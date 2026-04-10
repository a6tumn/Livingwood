plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.36.0"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "com.vanniktech.maven.publish")

    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(25)
    }

    mavenPublishing {
        publishToMavenCentral()
        signAllPublications()
    }
}