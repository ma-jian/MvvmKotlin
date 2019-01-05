package com.m.apt.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import dagger.Component;

import com.m.annotation.BindViewModel;
import com.m.annotation.InjectViewModel;
import com.m.apt.AnnotationProcessor;
import com.m.apt.IProcess;
import com.m.apt.util.ViewModelFactory;

/**
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 * 注解处理工具。 生产 ViewModule 对应的 Factory工厂
 * 添加注解 {@link BindViewModel,InjectViewModel}
 */
public class ViewModuleFactoryProcessor implements IProcess {

    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor processor) {

        try {
            String PACKAGENAME = "com.m.mvvmkotlin";
            String LIBPACKAGENAME = "com.m.library";
            String name = "ViewModelFactoryTools";
            String bindName = "bind";
            String bubildPackage = PACKAGENAME + ".bind";

            ClassName className = ClassName.get(bubildPackage, name);
            if (roundEnv.getElementsAnnotatedWith(InjectViewModel.class).isEmpty()) {
                return;
            }
            LinkedHashMap<Name, ClassName> linkedHashMap = new LinkedHashMap<>();
            String modelComponent = "ViewModelToolsComponent";
            TypeSpec.Builder componentBuilder = TypeSpec.interfaceBuilder(modelComponent)
                    .addJavadoc("@apt生产 dagger注入数据 {@link $T} \n", className)
                    .addAnnotation(Singleton.class)
                    .addAnnotation(AnnotationSpec.builder(Component.class)
//                            .addMember("modules", "{$T.class}", ClassName.get("com.sy.gristtown.di.module", "RetrofitModule"))
                            .build())
                    .addMethod(MethodSpec.methodBuilder("inject")
                            .addParameter(className, "viewModelTools")
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).build());

            JavaFile componentFile = JavaFile.builder(bubildPackage, componentBuilder.build()).build();
            componentFile.writeTo(processor.mFiler);// 在 app /build/generated/source/kapt 生成一份源代码

            //生成 ViewModelFactory
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(name)
                    .addJavadoc("@apt生产 $N View 绑定ViewModel 与 ViewModelFactory 的工具类\n", name)
                    .addModifiers(Modifier.PUBLIC);

            FieldSpec.Builder fieldBuilder = FieldSpec.builder(className, "mViewModelTools")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC);
            classBuilder.addField(fieldBuilder.build());

            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addJavadoc("@ dagger注入 {@link $T} \n", ClassName.get(bubildPackage, modelComponent))
                    .addStatement("$T.builder().build().inject(this)", ClassName.get(bubildPackage, "Dagger" + modelComponent));
            classBuilder.addMethod(constructorBuilder.build());

            MethodSpec.Builder instanceBuilder = MethodSpec.methodBuilder("getInstance")
                    .returns(className)
                    .addJavadoc("@ 获取 $N 单例\n", name)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addStatement("synchronized($N.class) { if (mViewModelTools == null) { mViewModelTools = new $T();}} return mViewModelTools", name, className);
            classBuilder.addMethod(instanceBuilder.build());

            MethodSpec.Builder activityMethodBuilder = MethodSpec.methodBuilder(bindName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get("androidx.appcompat.app", "AppCompatActivity"), "baseActivity");

            MethodSpec.Builder fragmentMethodBuilder = MethodSpec.methodBuilder(bindName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get("androidx.fragment.app", "Fragment"), "baseFragment");

            for (TypeElement typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(InjectViewModel.class))) {
                MethodSpec.Builder methodBuilder;
                String baseName;
                if (typeElement.getSuperclass().toString().contains("BaseActivity")) {
                    methodBuilder = activityMethodBuilder;
                    baseName = "baseActivity";
                } else {
                    methodBuilder = fragmentMethodBuilder;
                    baseName = "baseFragment";
                }

                List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
                for (VariableElement fieldElement : ElementFilter.fieldsIn(enclosedElements)) {
                    if (fieldElement.getKind() == ElementKind.FIELD && fieldElement.getAnnotation(BindViewModel.class) != null) {

                        //获取field类型
                        TypeElement element = (TypeElement) processor.mTypeUtils.asElement(fieldElement.asType());
                        //获取ViewModelFactory
                        ClassName classN;
                        if (linkedHashMap.get(element.getQualifiedName()) == null) {
                            //生成相应的ViewModelFactory文件
                            classN = ViewModelFactory.factory(element, processor);
                            linkedHashMap.put(element.getQualifiedName(), classN);

                            classBuilder.addField(FieldSpec.builder(classN, "m" + classN.simpleName())
                                    .addJavadoc("@ 注入 {@link $T} 对应的: {@link $T } \n", fieldElement.asType(), classN)
                                    .addAnnotation(Inject.class).build());
                        } else {
                            classN = linkedHashMap.get(element.getQualifiedName());
                        }

                        TypeName typeName = TypeName.get(typeElement.asType());
                        methodBuilder
                                .beginControlFlow("if($N instanceof $T)", baseName, typeName)
                                .addJavadoc("@ 绑定View {@link $T} 获取对应的 ViewModel: {@link $T} \n", typeElement.asType(), fieldElement.asType())
                                .addStatement("$T $N = ($T)$N", typeName, "m" + typeElement.getSimpleName().toString(), typeName, baseName)
                                .addStatement("$N.$N = $T.of($N, $N).get($T.class)",
                                        "m" + typeElement.getSimpleName().toString(),
                                        fieldElement.getSimpleName(),
                                        ClassName.get("androidx.lifecycle", "ViewModelProviders"),
                                        "m" + typeElement.getSimpleName().toString(),
                                        "m" + classN.simpleName(),
                                        TypeName.get(fieldElement.asType()))
                                .endControlFlow();
                    }
                }
            }

            classBuilder.addMethod(activityMethodBuilder.build());
            classBuilder.addMethod(fragmentMethodBuilder.build());
            JavaFile bindFile = JavaFile.builder(bubildPackage, classBuilder.build()).build();// 生成源代码
            bindFile.writeTo(processor.mFiler);
        } catch (Exception e) {
            error(processor.mMessager, e.getMessage());
            e.printStackTrace();
        }
    }

    private void log(Messager messager, String error) {
        messager.printMessage(Diagnostic.Kind.WARNING, this.getClass().getCanonicalName() + " : " + error);
    }

    private void error(Messager messager, String error) {
        messager.printMessage(Diagnostic.Kind.ERROR, this.getClass().getCanonicalName() + " : " + error);
    }
}
