package com.chrisgahlert.gradleguiceplugin.providers

import org.gradle.api.Project;

import com.chrisgahlert.gradleguiceplugin.GradleGuiceProjectScope;
import com.google.inject.Inject;
import com.google.inject.Provider;

class GradleProjectProvider implements Provider<Project>
{
    private GradleGuiceProjectScope scope;
    
    
    @Inject
    public GradleProjectProvider(GradleGuiceProjectScope scope)
    {
        this.scope = scope;
    }

    @Override
    public Project get()
    {
        return scope.getCurrent();
    }

}
