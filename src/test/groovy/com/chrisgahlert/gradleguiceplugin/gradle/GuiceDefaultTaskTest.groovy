package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.gradle.GuiceDefaultTask
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskAction

class GuiceDefaultTaskTest extends IntegrationSpec {

    def "default task should work"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            task test(type: ${TestTask.canonicalName}) {
            }
        """

        when:
        def result = runTasksSuccessfully 'test'

        then:
        result.standardOutput.contains 'abcd'
    }

    public static class TestModule implements Module {

        @Override
        void configure(Binder binder) {
            binder.bind(String).toInstance("abcd")
        }
    }

    public static class TestTask extends GuiceDefaultTask {
        @Inject String test
        @Inject Logger logger

        @TaskAction
        void run() {
            logger.warn test
        }
    }
}
