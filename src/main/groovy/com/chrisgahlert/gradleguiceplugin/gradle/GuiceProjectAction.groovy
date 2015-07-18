package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * This class can be used as a super class for project actions used in Gradle's
 * {@link Project#afterEvaluate(org.gradle.api.Action)}.
 *
 * All methods/members annotated with {@link com.google.inject.Inject} will be automatically injected before
 * the {@link GuiceProjectAction#doExecute(org.gradle.api.Project)} method is called.
 */
@CompileStatic
abstract class GuiceProjectAction implements Action<Project> {
    @Override
    final public void execute(Project project) {
        GradleInjector.inject(project, this)
        doExecute(project)
    }

    abstract public void doExecute(Project project)
}
