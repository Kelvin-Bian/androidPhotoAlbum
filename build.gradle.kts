// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.5")
        // Other classpath dependencies...
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
}