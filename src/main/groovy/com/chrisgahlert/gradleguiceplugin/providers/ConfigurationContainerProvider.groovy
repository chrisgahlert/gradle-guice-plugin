package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject;
import com.google.inject.Provider
import groovy.transform.CompileStatic;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer

@CompileStatic
class ConfigurationContainerProvider implements Provider<ConfigurationContainer>
{
    @Inject
    private Provider<Project> projectProvider

    @Override
    public ConfigurationContainer get()
    {
        projectProvider.get().configurations
    }

}
