package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import org.gradle.api.DefaultTask

/**
 * Created by Chris on 22.07.2015.
 */
class GuiceDefaultTask extends DefaultTask {

    @Override
    public void executeWithoutThrowingTaskFailure() {
        GradleInjector.inject(this, getProject())
        super.executeWithoutThrowingTaskFailure()
    }
}
