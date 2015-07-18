package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import com.google.inject.spi.InjectionPoint
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.tasks.TaskInputs
import org.gradle.logging.LoggingManagerInternal

import java.lang.reflect.Method
import java.util.logging.Filter
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger

/**
 * This task provides Guice dependency injection when used as a super class.
 *
 * All methods/members annotated with {@link com.google.inject.Inject} will be automatically injected before
 * the task is executed.
 */
@CompileStatic
class GuiceDefaultTask extends DefaultTask {
    private static class SkipGradleInjectionWarningsFilter implements Filter {
        @Override
        boolean isLoggable(LogRecord record) {
            if (record.level == Level.WARNING
                && record.message == "Method: {0} is not annotated with @Inject but " +
                    "is overriding a method that is annotated with @javax.inject.Inject.  Because " +
                    "it is not annotated with @Inject, the method will not be injected. " +
                    "To fix this, annotate the method with @Inject."
                && record.getParameters().length == 1
                && record.parameters[0] instanceof Method) {

                Method method = (Method) record.parameters[0]
                if (method.returnType.equals(LoggingManagerInternal)
                        || method.returnType.equals(TaskInputs)
                        || method.returnType.equals(TaskOutputsInternal)) {
                    return false
                }
            }

            return true
        }
    }

    GuiceDefaultTask() {
        super()

        def guiceLogger = Logger.getLogger(InjectionPoint.class.getName())
        guiceLogger.setFilter(new SkipGradleInjectionWarningsFilter())

        GradleInjector.inject(getProject(), this)
    }

}
