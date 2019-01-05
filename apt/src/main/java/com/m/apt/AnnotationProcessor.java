package com.m.apt;

import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.m.annotation.ApiFactory;
import com.m.annotation.InjectViewModel;
import com.m.apt.processor.ApiFactoryProcessor;
import com.m.apt.processor.ViewModuleFactoryProcessor;

/*
 * Created by majian
 * Date : 2018/6/25
 * Describe :
 */
@AutoService(Processor.class)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {
    public Elements mElements;
    public Filer mFiler;
    public Messager mMessager;
    public ProcessingEnvironment mProcessingEnvironment;
    public Types mTypeUtils;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        mElements = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
        mTypeUtils = processingEnv.getTypeUtils();
        mProcessingEnvironment = processingEnv;
        new ApiFactoryProcessor().process(roundEnv, this);
        new ViewModuleFactoryProcessor().process(roundEnv, this);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(ApiFactory.class.getCanonicalName());
        types.add(InjectViewModel.class.getCanonicalName());
        return types;
    }
}
