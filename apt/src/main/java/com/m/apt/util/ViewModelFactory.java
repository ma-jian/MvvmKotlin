package com.m.apt.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import com.m.apt.AnnotationProcessor;

/**
 * @Created by majian
 * @Date : 2018/12/23
 * @Describe :ViewModelFactory 工厂处理
 */

public class ViewModelFactory {
    public static ClassName factory(TypeElement typeElement, AnnotationProcessor processor) throws IOException {
        String qualifiedName = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        String CLASS_NAME = simpleName + "Factory";
        //生成源码位置
        String buildPackage = qualifiedName.replace(simpleName, "factory");
        ClassName className = ClassName.get(buildPackage, CLASS_NAME);

        ClassName superTypeName = ClassName.get("androidx.lifecycle", "ViewModelProvider", "NewInstanceFactory");
        TypeSpec.Builder tbClass = TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
//                .addAnnotation(AnnotationSpec.builder(AutoService.class).addMember("value", "$T.class", ClassName.get("androidx.lifecycle.ViewModelProvider", "Factory")).build())
                .addJavadoc("@ViewModelFactory工厂 此类由apt自动生成 {@link $T} 的工厂类 \n", ClassName.get(typeElement.asType()))
                .superclass(superTypeName);

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class); //添加daager2 注解

        StringBuilder paramsBuilder = new StringBuilder();

        //处理 viewmodel 参数
        for (ExecutableElement constructor : ElementFilter.constructorsIn(typeElement.getEnclosedElements())) {
            for (VariableElement variableElement : constructor.getParameters()) {
                FieldSpec field = FieldSpec.builder(TypeName.get(variableElement.asType()), variableElement.getSimpleName().toString()).addModifiers(Modifier.PRIVATE).build();
                tbClass.addField(field);
                constructorBuilder.addParameter(TypeName.get(variableElement.asType()), variableElement.getSimpleName().toString())
                        .addStatement(" this." + variableElement.getSimpleName().toString() + " = " + variableElement.getSimpleName().toString());

                paramsBuilder.append(variableElement.getSimpleName().toString()).append(",");
            }
        }


        TypeName typeName = ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T"));
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("create")
                .addTypeVariable(TypeVariableName.get("T", ClassName.get("androidx.lifecycle", "ViewModel")))
                .addJavadoc("@ {@link $T} 中的方法 \n", superTypeName)
                .addParameter(typeName, "modelClass")
                .returns(TypeVariableName.get("T"))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);


        if (!paramsBuilder.toString().isEmpty()) {
            paramsBuilder.replace(paramsBuilder.toString().length() - 1, paramsBuilder.toString().length(), "");
        }

        methodBuilder.addStatement("return ($T) new $T($N)", TypeVariableName.get("T"), ClassName.get(typeElement.asType()), paramsBuilder.toString());

        tbClass.addMethod(constructorBuilder.build());
        tbClass.addMethod(methodBuilder.build());

        //生成代码
        JavaFile javaFile = JavaFile.builder(buildPackage, tbClass.build()).build();// 生成源代码
        javaFile.writeTo(processor.mFiler);// 在 app module/build/generated/source/kapt/ 生成一份源代码
        return ClassName.get(buildPackage, CLASS_NAME);
    }
}
