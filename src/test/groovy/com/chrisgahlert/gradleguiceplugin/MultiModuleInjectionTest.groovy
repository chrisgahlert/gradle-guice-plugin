package com.chrisgahlert.gradleguiceplugin

import com.chrisgahlert.gradleguiceplugin.gradle.GuicePlugin
import com.google.inject.Binder
import com.google.inject.Inject
import com.google.inject.Module
import com.google.inject.Singleton
import nebula.test.IntegrationSpec
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

/**
 * Created by Chris on 21.07.2015.
 */
class MultiModuleInjectionTest extends IntegrationSpec {
    def setup() {
        logLevel = LogLevel.QUIET
    }

    def "sharing instances between modules should work"() {
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
        def result = runTasksSuccessfully ':sub:help'

        then:
        result.standardOutput.contains ':: first'
        result.standardOutput.contains ':sub: second'
    }

    public static class TestPlugin extends GuicePlugin {
        @Inject
        ISharedPojo pojo

        @Override
        void doApply(Project project) {
            project.logger.warn "$project.path: $pojo.name"
            pojo.name = 'second'
        }
    }

    public static interface ISharedPojo {
        String getName()
        void setName(String name);
    }

    @Singleton
    public static class SharedPojoImpl implements ISharedPojo {
        def name = "first"

        @Override
        String getName() {
            return name
        }

        @Override
        void setName(String name) {
            this.name = name
        }
    }

    public static class TestModule implements Module {

        @Override
        void configure(Binder binder) {
            binder.bind(ISharedPojo).to(SharedPojoImpl)
        }
    }
}
