package com.chrisgahlert.gradleguiceplugin

import com.chrisgahlert.gradleguiceplugin.annotations.ProjectScoped
import com.google.inject.Binder
import com.google.inject.Module

/**
 * Created by Chris on 22.07.2015.
 */
class GradleGuiceModule implements Module {
    @Override
    void configure(Binder binder) {
        def projectScope = new GradleGuiceProjectScope();
        binder.bindScope(ProjectScoped, projectScope)
        binder.bind(GradleGuiceProjectScope).toInstance(projectScope)
    }
}
