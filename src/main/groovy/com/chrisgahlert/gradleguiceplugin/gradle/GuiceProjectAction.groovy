package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Created by Chris on 22.07.2015.
 */
@CompileStatic
abstract class GuiceProjectAction implements Action<Project> {
    @Override
    final public void execute(Project project) {
        GradleInjector.inject(this, project)
        doExecute(project)
    }

    abstract public void doExecute(Project project)
}
