package com.m.plugin

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

/**
 * AspectjPlugin插件
 */
class AspectjPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.dependencies {
            implementation 'org.aspectj:aspectjrt:1.8.9'
        }
        final def loggr = project.logger
        loggr.error "-----------------------"
        loggr.error "AspectjPlugin 开始编织"
        loggr.error "-----------------------"
        project.android.applicationVariants.all { variant ->
            def javaCompile = variant.hasProperty('javaCompiler') ? variant.javaCompiler : variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString(),
                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                loggr.debug "ajc args: " + Arrays.toString(args)

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler)
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            loggr.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
                            loggr.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            loggr.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            loggr.debug message.message, message.thrown
                            break;
                    }
                }
            }
        }
    }
}