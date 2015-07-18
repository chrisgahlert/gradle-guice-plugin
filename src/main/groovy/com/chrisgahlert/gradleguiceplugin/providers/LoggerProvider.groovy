package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.logging.Logger

@CompileStatic
class LoggerProvider implements Provider<Logger> {

    @Inject
    private Provider<Project> projectProvider

    @Override
    Logger get() {
        projectProvider.get().logger
    }
}
