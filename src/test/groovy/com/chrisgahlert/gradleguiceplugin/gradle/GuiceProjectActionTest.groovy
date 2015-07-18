package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.gradle.GuiceDefaultTask
import com.chrisgahlert.gradleguiceplugin.gradle.GuiceProjectAction
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskAction

/**
 * Created by Chris on 23.07.2015.
 */
class GuiceProjectActionTest extends IntegrationSpec {

    def "project action should work"() {
        given:
        buildFile << """
            ext {
                guiceModule = '${TestModule.name}'
            }

            afterEvaluate(new ${TestProjectAction.canonicalName}())
        """

        when:
        def result = runTasksSuccessfully 'help'

        then:
        result.standardOutput.contains 'abcd'
    }

    public static class TestModule implements Module {

        @Override
        void configure(Binder binder) {
            binder.bind(String).toInstance("abcd")
        }
    }

    public static class TestProjectAction extends GuiceProjectAction {
        @Inject String test
        @Inject Logger logger

        @Override
        void doExecute(Project project) {
            logger.warn test
        }
    }
}
