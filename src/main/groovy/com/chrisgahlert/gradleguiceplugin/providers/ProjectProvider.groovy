package com.chrisgahlert.gradleguiceplugin.providers

import groovy.transform.CompileStatic
import org.gradle.api.Project;

import com.chrisgahlert.gradleguiceplugin.GradleGuiceProjectScope;
import com.google.inject.Inject;
import com.google.inject.Provider;

@CompileStatic
class ProjectProvider implements Provider<Project>
{
    @Inject
    private GradleGuiceProjectScope scope
    
    @Override
    public Project get()
    {
        return scope.current
    }

}
