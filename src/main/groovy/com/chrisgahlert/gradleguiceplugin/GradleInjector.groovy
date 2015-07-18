package com.chrisgahlert.gradleguiceplugin

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension

/**
 * Created by Chris on 22.07.2015.
 */
class GradleInjector {
    public static final String INJECTOR_PROPERTY = 'gradle-juice-plugin-injector'
    public static final String MODULE_PROPERTY = 'guiceModule'

    static public void inject(Object instance, Project context) {
        def injector = getInjector(context.rootProject.extensions.extraProperties);
        def scope = injector.getInstance(GradleGuiceProjectScope)

        scope.enter(scope)
        try {
            injector.injectMembers(instance)
        } finally {
            scope.leave()
        }
    }

    static protected Injector createInjector(ExtraPropertiesExtension props) {
        def moduleClass = props.get(MODULE_PROPERTY)
        if(!moduleClass) {
            throw new InvalidUserDataException("The extra-Property '$MODULE_PROPERTY' has not been set (gradle-guice-plugin)")
        }

        def moduleInstance;
        if(moduleClass instanceof Module) {
            moduleInstance = moduleClass
        } else if(moduleClass instanceof Class) {
            moduleInstance = moduleClass.newInstance()
        } else {
            moduleInstance = Class.forName(moduleClass.toString()).newInstance()
        }

        Guice.createInjector(new GradleGuiceModule(), moduleInstance)
    }

    static protected Injector getInjector(ExtraPropertiesExtension props) {
        if(!props.has(INJECTOR_PROPERTY)) {
            props.set(INJECTOR_PROPERTY, createInjector(props))
        }

        props.get(INJECTOR_PROPERTY) as Injector
    }
}
