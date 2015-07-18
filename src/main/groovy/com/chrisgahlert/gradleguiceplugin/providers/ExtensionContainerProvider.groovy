package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject
import com.google.inject.Provider
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer

/**
 * Created by Chris on 23.07.2015.
 */
@CompileStatic
class ExtensionContainerProvider implements Provider<ExtensionContainer> {

    @Inject
    private Provider<Project> projectProvider

    @Override
    ExtensionContainer get() {
        projectProvider.get().extensions
    }
}
