package com.chrisgahlert.gradleguiceplugin.providers

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.gradle.StartParameter
import org.gradle.api.Project;

class StartParametersProvider implements Provider<StartParameter>
{
    @Inject
    private Provider<Project> projectProvider;
    
    @Override
    public StartParameter get()
    {
        return projectProvider.get().getGradle().getStartParameter();
    }

}
