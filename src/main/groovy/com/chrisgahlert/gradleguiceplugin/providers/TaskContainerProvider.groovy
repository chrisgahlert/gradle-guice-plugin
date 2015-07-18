package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

@CompileStatic
class TaskContainerProvider implements Provider<TaskContainer> {

    @Inject
    private Provider<Project> projectProvider

    @Override
    TaskContainer get() {
        projectProvider.get().tasks
    }
}
