package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.gradle.StartParameter
import org.gradle.api.Project;

class StartParametersProvider implements Provider<StartParameter>
{
    private Provider<Project> projectProvider;
    
    @Inject
    public StartParametersProvider(Provider<Project> projectProvider)
    {
        this.projectProvider = projectProvider;
    }

    @Override
    public StartParameter get()
    {
        return projectProvider.get().getGradle().getStartParameter();
    }

}
