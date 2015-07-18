package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskInternal
import org.gradle.api.internal.tasks.TaskExecuter
import org.gradle.api.internal.tasks.TaskExecutionContext
import org.gradle.api.internal.tasks.TaskStateInternal

/**
 * This task provides Guice dependency injection when used as a super class.
 *
 * All methods/members annotated with {@link com.google.inject.Inject} will be automatically injected before
 * the task is executed.
 */
@CompileStatic
class GuiceDefaultTask extends DefaultTask {

    GuiceDefaultTask() {
        super()
        GradleInjector.inject(getProject(), this)
    }

}
