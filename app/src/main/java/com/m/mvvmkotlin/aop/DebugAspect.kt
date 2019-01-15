package com.m.mvvmkotlin.aop

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import java.util.concurrent.TimeUnit

/**
 * @Created by majian
 * @Date : 2019/1/14
 * @Describe :
 */
@Aspect
open class DebugAspect {
    @Pointcut("execution(@com.m.annotation.DebugLog * *(..))")
    fun methodAspect() {}

    @Around("methodAspect()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val simpleName = signature.declaringType.simpleName
        val name = signature.method.name
        val stringBuffer = StringBuffer()
        val start = System.nanoTime()
        val result = joinPoint.proceed()
        val end = System.nanoTime()
        for (parameter in joinPoint.args) {
            stringBuffer.append(parameter.toString()).append(", ")
        }
        Log.e("DebugLog : ", " DebugLog :$simpleName: $name ($stringBuffer); 耗时 ：${TimeUnit.NANOSECONDS.toMillis(end - start)} ms")
        return result
    }
}