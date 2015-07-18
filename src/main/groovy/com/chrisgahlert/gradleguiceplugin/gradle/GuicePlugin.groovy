package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by Chris on 21.07.2015.
 */
@CompileStatic
abstract public class GuicePlugin implements Plugin<Project> {

    @Override
    final public void apply(Project project) {
        GradleInjector.inject(this, project)
        doApply(project)
    }

    abstract public void doApply(Project project)
}
