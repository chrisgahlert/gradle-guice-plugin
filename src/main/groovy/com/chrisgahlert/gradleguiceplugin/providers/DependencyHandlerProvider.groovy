package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

@CompileStatic
class DependencyHandlerProvider implements Provider<DependencyHandler> {

    @Inject
    private Provider<Project> projectProvider

    @Override
    DependencyHandler get() {
        projectProvider.get().dependencies
    }
}
