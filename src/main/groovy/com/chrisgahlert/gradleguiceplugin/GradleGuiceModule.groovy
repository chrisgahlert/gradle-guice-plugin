package com.chrisgahlert.gradleguiceplugin

import com.chrisgahlert.gradleguiceplugin.providers.ArtifactHandlerProvider
import com.chrisgahlert.gradleguiceplugin.providers.DependencyHandlerProvider
import com.chrisgahlert.gradleguiceplugin.providers.ExtensionContainerProvider
import com.chrisgahlert.gradleguiceplugin.providers.LoggerProvider
import com.chrisgahlert.gradleguiceplugin.providers.PluginContainerProvider
import com.chrisgahlert.gradleguiceplugin.providers.RepositoryHandlerProvider
import com.chrisgahlert.gradleguiceplugin.providers.TaskContainerProvider
import org.gradle.StartParameter;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.invocation.Gradle

import com.chrisgahlert.gradleguiceplugin.annotations.ProjectScope
import com.chrisgahlert.gradleguiceplugin.providers.ConfigurationContainerProvider
import com.chrisgahlert.gradleguiceplugin.providers.ProjectProvider
import com.google.inject.Binder
import com.google.inject.Module

import groovy.transform.CompileStatic
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer

/**
 * Created by Chris on 22.07.2015.
 */
@CompileStatic
class GradleGuiceModule implements Module {
    private Gradle gradleInstance;
    
    public GradleGuiceModule(Project project)
    {
        this.gradleInstance = project.rootProject.gradle;
    }

    @Override
    void configure(Binder binder) {
        // bind the non-project-scope related instances
        binder.bind(Gradle).toInstance(gradleInstance)
        binder.bind(StartParameter).toInstance(gradleInstance.startParameter)
        binder.bind(TaskExecutionGraph).toInstance(gradleInstance.taskGraph)

        // setup the gradle project scope
        def projectScope = new GradleGuiceProjectScope()
        binder.bindScope(ProjectScope, projectScope)
        binder.bind(GradleGuiceProjectScope).toInstance(projectScope)

        // bind the project-scope related providers
        binder.bind(ArtifactHandler).toProvider(ArtifactHandlerProvider).in(ProjectScope)
        binder.bind(ConfigurationContainer).toProvider(ConfigurationContainerProvider).in(ProjectScope)
        binder.bind(DependencyHandler).toProvider(DependencyHandlerProvider).in(ProjectScope)
        binder.bind(ExtensionContainer).toProvider(ExtensionContainerProvider).in(ProjectScope)
        binder.bind(Logger).toProvider(LoggerProvider).in(ProjectScope)
        binder.bind(PluginContainer).toProvider(PluginContainerProvider).in(ProjectScope)
        binder.bind(Project).toProvider(ProjectProvider).in(ProjectScope)
        binder.bind(RepositoryHandler).toProvider(RepositoryHandlerProvider).in(ProjectScope)
        binder.bind(TaskContainer).toProvider(TaskContainerProvider).in(ProjectScope)
    }
}
