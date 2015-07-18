package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * This plugin should be used when using Guice.
 *
 * All methods/members annotated with {@link com.google.inject.Inject} will be automatically injected before
 * the {@link GuicePlugin#doApply(org.gradle.api.Project)} method is called.
 */
@CompileStatic
abstract public class GuicePlugin implements Plugin<Project> {

    @Override
    final public void apply(Project project) {
        GradleInjector.inject(project, this)
        doApply(project)
    }

    abstract public void doApply(Project project)
}
