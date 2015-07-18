package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler

@CompileStatic
class RepositoryHandlerProvider implements Provider<RepositoryHandler> {

    @Inject
    private Provider<Project> projectProvider

    @Override
    RepositoryHandler get() {
        projectProvider.get().repositories
    }
}
