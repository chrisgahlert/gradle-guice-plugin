package com.chrisgahlert.gradleguiceplugin.providers

import com.chrisgahlert.gradleguiceplugin.gradle.GuicePlugin
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer

class ProjectScopeProvidersTest extends IntegrationSpec {

    def setup() {
        logLevel = LogLevel.QUIET
    }

    def "the providers should get injected correctly in root project"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            apply plugin: ${TestPlugin.canonicalName}
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains ":: completed assertions"
    }

    def "the providers should get injected correctly in sub project"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }
        """

        addSubproject 'sub', """
            apply plugin: ${TestPlugin.canonicalName}
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains ":sub: completed assertions"
    }

    def "the providers should get injected correctly in multiple projects"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            apply plugin: ${TestPlugin.canonicalName}
        """

        addSubproject 'sub1', """
            apply plugin: ${TestPlugin.canonicalName}
        """

        addSubproject 'sub2', """
            apply plugin: ${TestPlugin.canonicalName}
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains ":: completed assertions"
        result.standardOutput.contains ":sub1: completed assertions"
        result.standardOutput.contains ":sub2: completed assertions"
    }

    public static class TestPlugin extends GuicePlugin {

        @Inject Project project

        @Inject ArtifactHandler artifacts
        @Inject ConfigurationContainer configurations
        @Inject DependencyHandler dependencies
        @Inject ExtensionContainer extensions
        @Inject Logger logger
        @Inject PluginContainer plugins
        @Inject RepositoryHandler repositories
        @Inject TaskContainer tasks

        @Override
        void doApply() {
            assert project.artifacts.is(artifacts)
            assert project.configurations.is(configurations)
            assert project.dependencies.is(dependencies)
            assert project.extensions.is(extensions)
            assert project.logger.is(logger)
            assert project.plugins.is(plugins)
            assert project.repositories.is(repositories)
            assert project.tasks.is(tasks)

            logger.warn "${project.path}: completed assertions"
        }
    }

    public static class TestModule implements Module {
        @Override
        void configure(Binder binder) {
        }
    }
}
