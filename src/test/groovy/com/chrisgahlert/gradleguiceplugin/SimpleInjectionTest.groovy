package com.chrisgahlert.gradleguiceplugin

import com.chrisgahlert.gradleguiceplugin.gradle.GuicePlugin
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

/**
 * Created by Chris on 21.07.2015.
 */
class SimpleInjectionTest extends IntegrationSpec {

    def setup() {
        logLevel = LogLevel.QUIET
    }

    def "basic string injection should work"() {
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
        result.standardOutput.contains "teststr"
    }

    public static class TestPlugin extends GuicePlugin {

        @Inject
        private String test;

        @Override
        void doApply(Project project) {
            project.logger.warn test
        }
    }

    public static class TestModule implements Module {

        @Override
        void configure(Binder binder) {
            binder.bind(String).toInstance("teststr")
        }
    }
}
