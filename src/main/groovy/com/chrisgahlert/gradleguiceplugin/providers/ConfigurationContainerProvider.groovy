package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer

class ConfigurationContainerProvider implements Provider<ConfigurationContainer>
{
    @Inject
    private Provider<Project> projectProvider;

    @Override
    public ConfigurationContainer get()
    {
        return projectProvider.get().getConfigurations();
    }

}
