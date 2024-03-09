// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript{
    repositories{
        mavenCentral()
    }
    dependencies{
        classpath ("com.google.gms:google-services:4.3.3")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.46")
        classpath ("com.android.tools.build:gradle:7.2.2")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
//    id("com.google.dagger.hilt.android") version "2.44" apply false
//    id("com.google.gms.google-services") version "4.4.0" apply false
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
