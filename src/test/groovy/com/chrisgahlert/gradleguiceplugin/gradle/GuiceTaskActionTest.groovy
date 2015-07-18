package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.gradle.GuiceProjectAction
import com.chrisgahlert.gradleguiceplugin.gradle.GuiceTaskAction
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger

class GuiceTaskActionTest extends IntegrationSpec {

    def "task action should work"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            task test {}

            test.doLast new ${TestTaskAction.canonicalName}()
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

    public static class TestTaskAction extends GuiceTaskAction {
        @Inject String test
        @Inject Logger logger

        @Override
        void doExecute(Task task) {
            logger.warn test
        }
    }
}
