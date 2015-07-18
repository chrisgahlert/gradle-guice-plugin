package com.chrisgahlert.gradleguiceplugin

import com.google.inject.Binder
import com.google.inject.Module
import nebula.test.IntegrationSpec

class GradleInjectorTest extends IntegrationSpec {

    def "get instance should work and the injector should take a string argument"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            logger.warn ${GradleInjector.canonicalName}.getInstance(project, String)
            assert project.is(${GradleInjector.canonicalName}.getInstance(project, Project))
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains 'abc'
    }

    def "the injector should take an instantiated module"() {
        given:
        buildFile << """
            ext {
                guiceModule = new com.google.inject.Module() {
                    @Override
                    void configure(com.google.inject.Binder binder) {
                        binder.bind(String.class).toInstance('instantiated-directly')
                    }
                }
            }

            logger.warn ${GradleInjector.canonicalName}.getInstance(project, String)
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains 'instantiated-directly'
    }

    def "the injector should take a class object"() {
        given:
        buildFile << """
            ext {
                guiceModule = ${TestModule.canonicalName}
            }

            logger.warn ${GradleInjector.canonicalName}.getInstance(project, String)
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains 'abc'
    }

    def "should throw error if the module has not been set"() {
        given:
        buildFile << """
            apply plugin: 'com.chrisgahlert.gradle-guice-plugin'
        """

        when:
        def result = runTasksWithFailure 'help'

        then:
        result.standardError.contains "Cannot get property 'guiceModule' on extra properties extension as it does not exist"
    }

    def "should throw error if the module is empty"() {
        given:
        buildFile << """
            ext {
                guiceModule = ''
            }

            apply plugin: 'com.chrisgahlert.gradle-guice-plugin'
        """

        when:
        def result = runTasksWithFailure 'help'

        then:
        result.standardError.contains "The extra-Property 'guiceModule' has not been set (gradle-guice-plugin)"
    }

    public static class TestModule implements Module {

        @Override
        void configure(Binder binder) {
            binder.bind(String.class).toInstance('abc')
        }
    }

}