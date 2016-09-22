# gradle-guice-plugin (discontinued)

This plugin gives Gradle plugin developers the ability to use Guice dependency injection. This is
especially useful in enterprise contexts where very large and complex Gradle plugins are being
created.

## Getting started

### Setup the plugin project

Add the following dependency to your plugin project's `build.gradle` file:
```gradle
repositories {
    maven {
        url "https://plugins.gradle.org/m2/"
    }
}
dependencies {
    compile "gradle.plugin.com.chrisgahlert:gradle-guice-plugin:+"
}
```

Now you can make your plugins `extend` from the `GuicePlugin` class instead of implementing `Plugin<Project>`:
```groovy
class MyPlugin extends GuicePlugin {
    @Inject Project project
    @Inject MyInterface myInterface
    @Inject MyOtherInterface myOtherInterface
    
    @Override
    void doApply() {
        // your plugin code
    }
}
```

Next you should create a Guice module class:
```groovy
class MyModule implements Module {
    @Override
    void configure(Binder binder) {
        // create your custom bindings, e.g.
        binder.bind(MyInterface).to(MyInterfaceImpl).in(ProjectScope)
        binder.bind(MyOtherInterface).to(MyOtherInterfaceImpl).in(Singleton)
    }
}
```

### Setup the consumer of your plugin

Include the following snippet in the rootProject's `build.gradle`:
```gradle
ext {
    guiceModule = 'com.example.MyModule'
}

apply plugin: com.example.MyPlugin
```

And now you can start developing...

## Scopes

Additionally to the scopes brought by Guice, there is the `@ProjectScope`. Everything bound to this scope
will be unique within a Gradle project. This means that within a multi module Gradle build, each module will
have it's own unique instance.

```groovy
@ProjectScope
class MyAnnotatedService {
    @Inject Logger logger
}

class MyService {
    @Inject Logger logger
}

class MyModule implements Module {
    void configure(Binder binder) {
        bind(MyAnnotatedService)
        bind(MyService).in(ProjectScope)
    }
}
```

## Included super types

The following super classes are available to use:

### GuicePlugin

```groovy
class MyPlugin extends GuicePlugin {
    @Inject Gradle gradle
    @Inject StartParameter startParameter
    @Inject TaskExecutionGraph taskGraph

    @Inject Project project

    @Inject ArtifactHandler artifacts
    @Inject ConfigurationContainer configurations
    @Inject DependencyHandler dependencies
    @Inject ExtensionContainer extensions
    @Inject Logger logger
    @Inject PluginContainer plugins
    @Inject RepositoryHandler repositories
    @Inject TaskContainer tasks

    @Override
    void doApply() {
        // your plugin code
    }
}
```

### GuiceDefaultTask

```groovy
class MyTask extends GuiceDefaultTask {
    @Inject MyService service
    @Inject Project project
    
    @TaskAction
    void run() {
        // your code here
    }
}
```

### GuiceProjectAction

This is a convenience action. You could also let Guice create the actions for you. Then you wouldn't need to use this super type.

```groovy
class MyProjectAction extends GuiceProjectAction {
    @Inject MyService service
    @Inject Project project
    
    void doExecute() {
        // your code here
    }
}

// somewhere else
project.afterEvaluate(new MyProjectAction())
```

### GuiceTaskAction

This is a convenience action. You could also let Guice create the actions for you. Then you wouldn't need to use this super type.

```groovy
class MyTaskAction extends GuiceTaskAction {
    @Inject MyService service
    @Inject Project project
    
    void doExecute() {
        // your code here
    }
}

// somewhere else
tasks.jar.doFirst(new MyTaskAction()) // or
tasks.jar.doLast(new MyTaskAction())
```

## Using the injector directly

In order to e.g. get instances of another project, you can use the `GradleInjector`.

```groovy
def myServiceInstance = GradleInjector.getInstance(project, MyServiceClass);
```

In case you want to make use of your own super classes, you just need to make sure that the following
snippet of code is called before the actual code is used:

```groovy
class MyCustomZipTask extends Zip {
    @Inject MyService service
    @Inject Project project

    public MyCustomZipTask() {
        super()
        GradleInjector.injectMembers(getProject(), this)

        from('...')
        into('...')
    }
}
```

## Known limitations

* Currently only one plugin would be able to make use of this plugin.
