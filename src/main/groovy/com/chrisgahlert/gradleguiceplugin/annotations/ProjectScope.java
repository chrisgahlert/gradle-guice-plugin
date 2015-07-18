package com.chrisgahlert.gradleguiceplugin.annotations;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation makes use of the Gradle project scope in Guice dependency injection.
 * All classes annotated with this will only have 1 instance per Gradle Project.
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface ProjectScope {
}
