plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.maven.publish)
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.vanniktech.maven.publish")

    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(25)
    }

    mavenPublishing {
        publishToMavenCentral()
        signAllPublications()
    }
}
