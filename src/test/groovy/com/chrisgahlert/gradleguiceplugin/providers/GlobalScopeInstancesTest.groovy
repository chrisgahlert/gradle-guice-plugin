package com.chrisgahlert.gradleguiceplugin.providers

import com.chrisgahlert.gradleguiceplugin.gradle.GuicePlugin
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.invocation.Gradle
import org.gradle.api.logging.LogLevel

class GlobalScopeInstancesTest extends IntegrationSpec {

    def setup() {
        logLevel = LogLevel.QUIET
    }

    def "the global instances should get injected correctly"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            apply plugin: ${TestPlugin.canonicalName}
        """

        addSubproject 'sub', """
            apply plugin: ${TestPlugin.canonicalName}
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains ":: completed assertions"
        result.standardOutput.contains ":sub: completed assertions"
    }

    public static class TestPlugin extends GuicePlugin {

        @Inject Gradle gradle
        @Inject StartParameter startParameter
        @Inject TaskExecutionGraph taskGraph

        @Override
        void doApply(Project project) {
            assert project.gradle.is(gradle)
            assert project.gradle.startParameter.is(startParameter)
            assert project.gradle.taskGraph.is(taskGraph)

            project.logger.warn "${project.path}: completed assertions"
        }
    }

    public static class TestModule implements Module {
        @Override
        void configure(Binder binder) {
        }
    }
}