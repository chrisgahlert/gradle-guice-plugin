package com.chrisgahlert.gradleguiceplugin.gradle

import groovy.transform.CompileStatic
import org.gradle.api.Project

/**
 * This is a dummy plugin, as publishing to plugins.gradle.org requires at least one plugin.
 * Thus this plugin should never be used.
 */
@CompileStatic
class DefaultPlugin extends GuicePlugin {
    @Override
    void doApply(Project project) {
    }
}
