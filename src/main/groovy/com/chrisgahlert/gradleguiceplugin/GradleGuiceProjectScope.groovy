package com.chrisgahlert.gradleguiceplugin

import com.google.inject.Key
import com.google.inject.Provider
import com.google.inject.Scope
import com.google.inject.Scopes
import groovy.transform.CompileStatic
import org.gradle.api.Project

/**
 * Created by Chris on 22.07.2015.
 */
class GradleGuiceProjectScope implements Scope {
    public static final String SCOPE_PROPERTY = 'gradle-guice-plugin-scope'
    private ThreadLocal<Project> current = new ThreadLocal()

    @Override
    def Provider scope(Key key, Provider unscoped) {
        return new Provider() {
            @Override
            def get() {
                def scope = getProjectScope();
                def instance = scope.get(key)

                if(instance == null && !scope.containsKey(key)) {
                    instance = unscoped.get()

                    if(!Scopes.isCircularProxy(instance)) {
                        scope.put(key, instance)
                    }
                }

                instance
            }
        }
    }

    private Map<Key, Object> getProjectScope() {
        def extraProps = getCurrent().extensions.extraProperties

        if(!extraProps.has(SCOPE_PROPERTY)) {
            extraProps.set(SCOPE_PROPERTY, new HashMap<Key, Object>())
        }

        extraProps.get(SCOPE_PROPERTY) as Map<Key, Object>
    }

    public void enter(Project current) {
        this.current.set(current)
    }

    public void leave() {
        this.current.remove()
    }
    
    public Project getCurrent() {
        def project = current.get()
        
        if(project == null) {
            throw new RuntimeException("Illegal project scope access outside of being in a project scope")
        }
        
        project
    }
}
