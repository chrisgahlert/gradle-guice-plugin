package com.chrisgahlert.gradleguiceplugin

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension

/**
 * This class is the actual injector which makes use of Gradle project scope.
 *
 * @see GradleGuiceProjectScope
 * @see com.chrisgahlert.gradleguiceplugin.annotations.ProjectScope
 */
@CompileStatic
class GradleInjector {
    public static final String INJECTOR_PROPERTY = 'gradle-juice-plugin-injector'
    public static final String MODULE_PROPERTY = 'guiceModule'

    /**
     * Injects all methods/fields annotated with {@link com.google.inject.Inject}.
     *
     * @param context The Gradle project scope to use
     * @param instance The instance whose methods/fields should be injected
     */
    static public void inject(Project context, Object instance) {
        def injector = getInjector(context);
        def scope = injector.getInstance(GradleGuiceProjectScope)

        scope.enter(context)
        try {
            injector.injectMembers(instance)
        } finally {
            scope.leave()
        }
    }

    /**
     * Get's an instance for the injector.
     *
     * @param context The Gradle project scope to use
     * @param instanceClass The requested type which should be passed to Guice
     * @return
     */
    static public <T> T getInstance(Project context, Class<T> instanceClass) {
        def injector = getInjector(context);
        def scope = injector.getInstance(GradleGuiceProjectScope)

        scope.enter(context)
        try {
            return injector.getInstance(instanceClass)
        } finally {
            scope.leave()
        }
    }

    static protected Injector createInjector(Project context) {
        ExtraPropertiesExtension props = context.rootProject.extensions.extraProperties
        
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

        Guice.createInjector(new GradleGuiceModule(context), (Module) moduleInstance)
    }

    static protected Injector getInjector(Project context) {
        ExtraPropertiesExtension props = context.rootProject.extensions.extraProperties
        
        if(!props.has(INJECTOR_PROPERTY)) {
            props.set(INJECTOR_PROPERTY, createInjector(context))
        }

        props.get(INJECTOR_PROPERTY) as Injector
    }
}
