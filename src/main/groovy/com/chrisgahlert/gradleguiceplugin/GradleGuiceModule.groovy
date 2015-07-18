package com.chrisgahlert.gradleguiceplugin

import org.gradle.StartParameter;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.invocation.Gradle

import com.chrisgahlert.gradleguiceplugin.annotations.ProjectScoped
import com.chrisgahlert.gradleguiceplugin.providers.ConfigurationContainerProvider
import com.chrisgahlert.gradleguiceplugin.providers.GradleProjectProvider;
import com.chrisgahlert.gradleguiceplugin.providers.StartParametersProvider;
import com.google.inject.Binder
import com.google.inject.Module

import groovy.transform.CompileStatic

/**
 * Created by Chris on 22.07.2015.
 */
@CompileStatic
class GradleGuiceModule implements Module {
    private Project rootProject;
    
    public GradleGuiceModule(Project project)
    {
        this.rootProject = project.rootProject;
    }

    @Override
    void configure(Binder binder) {
        def projectScope = new GradleGuiceProjectScope();
        binder.bindScope(ProjectScoped, projectScope)
        binder.bind(GradleGuiceProjectScope).toInstance(projectScope)
        
        binder.bind(Project).toProvider(GradleProjectProvider).in(ProjectScoped)
        binder.bind(ConfigurationContainer).toProvider(ConfigurationContainerProvider).in(ProjectScoped)
        binder.bind(Gradle).toInstance(rootProject.gradle)
        binder.bind(StartParameter).toInstance(rootProject.gradle.startParameter)
    }
}
