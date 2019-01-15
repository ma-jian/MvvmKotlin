package com.m.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Created by majian
 * @Date : 2019/1/14
 * @Describe :
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface DebugLog {
}
