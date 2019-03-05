package com.m.apt.processor;

import com.m.annotation.ApiFactory;
import com.m.apt.AnnotationProcessor;
import com.m.apt.IProcess;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import dagger.Component;

/**
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 * 注解处理工具。 生产retrofit 接口处理类
 * 添加注解 {@link ApiFactory}
 */

public class ApiFactoryProcessor implements IProcess {
    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor processor) {
        try {
            /**
             * 生成网络管理类
             */
            String PACKAGENAME = "com.m.mvvmkotlin";
            String LIBPACKAGENAME = "com.m.library";

            for (TypeElement typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(ApiFactory.class))) {
                String CLASS_NAME = typeElement.getSimpleName().toString() + "Factory";
                String typePackageName = typeElement.getQualifiedName().toString();
                String className = typeElement.getSimpleName().toString();
                /**
                 * 生产类，构造方法
                 */
                TypeName typeName = ParameterizedTypeName.get(ClassName.get(LIBPACKAGENAME + ".http", "BaseHttpFactory"), TypeName.get(typeElement.asType()));

                TypeSpec.Builder tb = TypeSpec.classBuilder(CLASS_NAME)
                        .addModifiers(Modifier.PUBLIC)
                        .addJavadoc("@API工厂 此类由apt自动生成 {@link $T} 的接口实现类 \n", TypeName.get(typeElement.asType()))
                        .superclass(typeName);

                MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ClassName.get(LIBPACKAGENAME + ".http", "RetrofitInterface"), "retrofitInterface")
                        .addAnnotation(Inject.class)
                        .addStatement(" super($N)", "retrofitInterface");

                //方法处理
                for (ExecutableElement methodElement : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
                    String resultFuncType = "";
                    String serverDataFuncType = "";
                    TypeName methodReturnType = TypeName.get(methodElement.getReturnType());
                    TypeName resultType;
                    ClassName resultRawType = null;
                    ClassName rawType = null;
                    TypeName wrapType;
                    if (methodReturnType instanceof ParameterizedTypeName) {
                        wrapType = ((ParameterizedTypeName) methodReturnType).typeArguments.get(0);
                        rawType = ((ParameterizedTypeName) methodReturnType).rawType;
                        if (wrapType instanceof ParameterizedTypeName) {
                            ClassName type = ((ParameterizedTypeName) wrapType).rawType;
                            if (type.equals(ClassName.get(LIBPACKAGENAME + ".http", "HttpResHeaderList"))) {
                                resultType = ((ParameterizedTypeName) wrapType).typeArguments.get(0);
                                resultFuncType = "HttpResultListFunc";
                                serverDataFuncType = "ServerDataListFunc";
                                resultRawType = type;
                            } else if (type.equals(ClassName.get(LIBPACKAGENAME + ".http", "HttpResHeader"))) {
                                resultType = ((ParameterizedTypeName) wrapType).typeArguments.get(0);
                                resultFuncType = "HttpResultFunc";
                                serverDataFuncType = "ServerDataFunc";
                                resultRawType = type;
                            } else {
                                //特殊情况 全部返回请求数据
                                resultType = wrapType;
                            }
                        } else {
                            resultType = wrapType;
                        }
                    } else {
                        error(processor.mMessager, "必须使用Flowable 包装数据");
                        return;
                    }

                    TypeName returnTypeName;
                    //返回值类型
                    if (resultRawType == null || resultRawType.equals(ClassName.get(LIBPACKAGENAME + ".http", "HttpResHeader"))) {
                        returnTypeName = ParameterizedTypeName.get(rawType, resultType);
                    } else if (resultRawType.equals(ClassName.get(LIBPACKAGENAME + ".http", "HttpResHeaderList"))) {
                        returnTypeName = ParameterizedTypeName.get(rawType, ParameterizedTypeName.get(ClassName.get("java.util", "ArrayList"), resultType));
                    } else {
                        returnTypeName = ParameterizedTypeName.get(rawType, resultType);
                    }

                    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodElement.getSimpleName().toString())
                            .addJavadoc("@apt生产的接口方法 {@link $T#$N} \n", TypeName.get(typeElement.asType()), methodElement.getSimpleName())
                            .returns(returnTypeName)
                            .addModifiers(Modifier.PUBLIC);

                    //参数
                    StringBuilder paramsBuilder = new StringBuilder();
                    for (VariableElement variableElement : methodElement.getParameters()) {
                        methodBuilder.addParameter(TypeName.get(variableElement.asType()), variableElement.getSimpleName().toString());
                        paramsBuilder.append(variableElement.getSimpleName().toString()).append(",");
                    }

                    if (!paramsBuilder.toString().isEmpty()) {
                        paramsBuilder.replace(paramsBuilder.toString().length() - 1, paramsBuilder.toString().length(), "");
                    }

                    /**
                     * 处理数据
                     */
                    if (resultRawType == null) {
                        methodBuilder.addStatement("return $N.$N($L).compose($T.Companion.applyAsync())",
                                "mObtainService",
                                methodElement.getSimpleName().toString(),
                                paramsBuilder.toString(),
                                ClassName.get(LIBPACKAGENAME + ".utils", "RxThread"));
                    } else {
                        methodBuilder.addStatement("return $N.$N($L).map(new $T<$T>()).onErrorResumeNext(new $T<$T>()).compose($T.Companion.applyAsync())",
                                "mObtainService",
                                methodElement.getSimpleName().toString(),
                                paramsBuilder.toString(),
                                ClassName.get(LIBPACKAGENAME + ".http", serverDataFuncType),
                                resultType,
                                ClassName.get(LIBPACKAGENAME + ".http", resultFuncType),
                                resultType,
                                ClassName.get(LIBPACKAGENAME + ".utils", "RxThread"));
                    }

                    tb.addMethod(methodBuilder.build());
                }
                tb.addMethod(constructorBuilder.build());
                //生成源码位置
                String buildPackage = typePackageName.replace("." + className, "");
                JavaFile javaFile = JavaFile.builder(buildPackage, tb.build()).build();
                javaFile.writeTo(processor.mFiler);// 在 app /build/generated/source/kapt 生成一份源代码
            }


            if (!roundEnv.getElementsAnnotatedWith(ApiFactory.class).isEmpty()) {
                String baseRepositoryPackage = PACKAGENAME + ".bind";
                String interComponent = "BaseRepositoryComponent";
                TypeSpec.Builder componentBuilder = TypeSpec.interfaceBuilder(interComponent)
                        .addAnnotation(Singleton.class)
                        .addAnnotation(AnnotationSpec.builder(Component.class)
                                .addMember("modules", "{$T.class}", ClassName.get(LIBPACKAGENAME + ".di.module", "RetrofitModule")).build())
                        .addMethod(MethodSpec.methodBuilder("inject").addParameter(ClassName.get(baseRepositoryPackage, "BaseRepository"), "repository")
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).build());

                JavaFile componentFile = JavaFile.builder(baseRepositoryPackage, componentBuilder.build()).build();
                componentFile.writeTo(processor.mFiler);// 在 app /build/generated/source/kapt 生成一份源代码

                TypeSpec.Builder classBuilder = TypeSpec.classBuilder("BaseRepository")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addJavadoc("@API工厂此类由apt自动生成 数据层基类 \n")
                        .addSuperinterface(ClassName.get(LIBPACKAGENAME + ".http", "RepositoryInterface"));

                MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                        .addStatement("this.mSubscription = new $T()", ClassName.get("io.reactivex.disposables", "CompositeDisposable"))
                        .addStatement("$T.builder().build().inject(this)", ClassName.get(baseRepositoryPackage, "Dagger" + interComponent))
                        .addModifiers(Modifier.PUBLIC);

                FieldSpec disposiableField = FieldSpec.builder(ClassName.get("io.reactivex.disposables", "CompositeDisposable"), "mSubscription", Modifier.PUBLIC).build();
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onCleared")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addAnnotation(Override.class)
                        .addStatement(" mSubscription.clear()");
                for (TypeElement typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(ApiFactory.class))) {
                    String typePackageName = typeElement.getQualifiedName().toString();
                    String className = typeElement.getSimpleName().toString();
                    String buildPackage = typePackageName.replace("." + className, "");
                    FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(buildPackage, typeElement.getSimpleName().toString() + "Factory"), "m" + typeElement.getSimpleName() + "Repository", Modifier.PUBLIC)
                            .addAnnotation(Inject.class)
                            .addJavadoc("@apt生产 注入 @{@link $T} 的网络请求工厂 \n", typeElement.asType()).build();
                    classBuilder.addField(fieldSpec);
                }

                classBuilder.addField(disposiableField);
                classBuilder.addMethod(constructorBuilder.build());
                classBuilder.addMethod(methodBuilder.build());
                JavaFile javaFile = JavaFile.builder(baseRepositoryPackage, classBuilder.build()).build();
                javaFile.writeTo(processor.mFiler);// 在 app /build/generated/source/kapt 生成一份源代码
            }
        } catch (Exception e) {
            error(processor.mMessager, e.getMessage());
            e.printStackTrace();
        }
    }

    private void error(Messager messager, String error) {
        messager.printMessage(Diagnostic.Kind.ERROR, this.getClass().getCanonicalName() + " : " + error);
    }
}