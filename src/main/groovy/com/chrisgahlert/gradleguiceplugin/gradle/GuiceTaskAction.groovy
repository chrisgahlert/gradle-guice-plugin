package com.chrisgahlert.gradleguiceplugin.gradle

import com.chrisgahlert.gradleguiceplugin.GradleInjector
import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Task

/**
 * Created by Chris on 22.07.2015.
 */
@CompileStatic
abstract class GuiceTaskAction implements Action<Task> {
    @Override
    final public void execute(Task task) {
        GradleInjector.inject(this, task.getProject())
        doExecute(task)
    }

    abstract public void doExecute(Task task)
}
