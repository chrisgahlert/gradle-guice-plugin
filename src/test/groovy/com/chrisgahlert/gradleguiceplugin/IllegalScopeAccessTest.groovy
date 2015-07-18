package com.chrisgahlert.gradleguiceplugin

import com.chrisgahlert.gradleguiceplugin.gradle.GuicePlugin
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.api.Project

/**
 * Created by Chris on 24.07.2015.
 */
class IllegalScopeAccessTest extends IntegrationSpec {

    def "should throw error if project is requested outside of project scope"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            apply plugin: 'com.chrisgahlert.gradle-guice-plugin'
        """

        when:
        def result = runTasksWithFailure 'help'

        then:
        result.standardError.contains "Illegal project scope access while not being in a project scope"
    }

    public static class TestSingleton {
        @Inject Project project
    }

    public static class TestModule implements Module {

        @Override
        void configure(Binder binder) {
            binder.bind(TestSingleton).asEagerSingleton()
        }
    }
}
