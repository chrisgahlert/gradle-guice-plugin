package com.chrisgahlert.gradleguiceplugin

import com.chrisgahlert.gradleguiceplugin.annotations.ProjectScoped
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
class ProjectScopeInjectionTest extends IntegrationSpec {
    def setup() {
        logLevel = LogLevel.QUIET
    }

    def "using gradle project scope should work"() {
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
        result.standardOutput.contains ':: first!'
        result.standardOutput.contains ':sub: first!'
    }

    public static class TestPlugin extends GuicePlugin {
        @Inject
        ISharedPojo pojo

        @Override
        void doApply(Project project) {
            project.logger.warn "$project.path: $pojo.name"
        }

        @Inject
        public void injectPojo(ISharedPojo pojo2) {
            pojo2.name += '!'
        }
    }

    public static interface ISharedPojo {
        String getName()
        void setName(String name);
    }

    @ProjectScoped
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
