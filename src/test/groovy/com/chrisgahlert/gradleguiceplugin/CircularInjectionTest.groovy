package com.chrisgahlert.gradleguiceplugin

import com.chrisgahlert.gradleguiceplugin.annotations.ProjectScope
import com.chrisgahlert.gradleguiceplugin.gradle.GuicePlugin
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import nebula.test.IntegrationSpec
import org.gradle.api.Project

class CircularInjectionTest extends IntegrationSpec {

    def "circular injection should work"() {
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
        result.standardOutput.contains "assertions completed"
    }

    @ProjectScope
    public static class A {
        @Inject final public B b
    }

    @ProjectScope
    public static class B {
        @Inject final public A a
    }

    public static class TestPlugin extends GuicePlugin {

        @Inject A localA
        @Inject B localB

        @Override
        void doApply(Project project) {
            assert localA.b.is(localB)
            assert localB.a.is(localA)

            project.logger.warn "assertions completed"
        }
    }

    public static class TestModule implements Module {

        @Override
        void configure(Binder binder) {
            binder.bind(A)
            binder.bind(B)
        }
    }

}
