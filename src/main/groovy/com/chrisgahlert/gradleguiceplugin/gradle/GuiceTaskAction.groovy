package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Task

/**
 * This class can be used as a super class for task actions used in Gradle tasks'
 * {@link Task#doFirst(org.gradle.api.Action)} and {@link Task#doLast(org.gradle.api.Action)} methods..
 *
 * All methods/members annotated with {@link com.google.inject.Inject} will be automatically injected before
 * the {@link GuiceTaskAction#doExecute(org.gradle.api.Task)} method is called.
 */
@CompileStatic
abstract class GuiceTaskAction implements Action<Task> {
    @Override
    final public void execute(Task task) {
        GradleInjector.inject(task.getProject(), this)
        doExecute(task)
    }

    abstract public void doExecute(Task task)
}
