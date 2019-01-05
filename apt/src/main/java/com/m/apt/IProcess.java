package com.m.apt;

import javax.annotation.processing.RoundEnvironment;

/*
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */
public interface IProcess {
    void process(RoundEnvironment roundEnv, AnnotationProcessor processor);
}
