plugins {
    alias(libs.plugins.fabric.loom)
}

dependencies {
    minecraft(libs.minecraft)
    listOf(libs.fabricLoader, libs.fabricApi, libs.fabricKotlin).forEach(::implementation)
}

loom {
    splitEnvironmentSourceSets()
}

val mainSourceSet = sourceSets["main"]
val clientSourceSet = sourceSets["client"]

val dataSourceSet = sourceSets.create("data") {
    kotlin.srcDir("src/data/kotlin")
    resources.srcDir("src/data/resources")

    compileClasspath += mainSourceSet.compileClasspath + mainSourceSet.output
    compileClasspath += clientSourceSet.compileClasspath + clientSourceSet.output
    runtimeClasspath += mainSourceSet.runtimeClasspath + mainSourceSet.output
    runtimeClasspath += clientSourceSet.runtimeClasspath + clientSourceSet.output
}

loom {
    mods {
        register("liveroot") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
            sourceSet(sourceSets["data"])
        }
    }
}

tasks.processResources {
    inputs.property("version", libs.versions.mod.version)
    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

tasks.withType<JavaCompile>().configureEach { options.release = 25 }

java { sourceCompatibility = JavaVersion.VERSION_25; targetCompatibility = JavaVersion.VERSION_25 }

tasks.jar {
    from(dataSourceSet.output)
    inputs.property("archivesName", libs.versions.archives.base.name)
    from("LICENSE") { rename { "${it}_${libs.versions.archives.base.name}" } }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = libs.versions.maven.group.get()
            artifactId = "lib"
            version = libs.versions.mod.version.get()
        }
    }
   repositories { mavenLocal() }
}
