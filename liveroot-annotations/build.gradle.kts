repositories {
    mavenCentral()
}

publishing {
    publications {
        register<MavenPublication>("MavenJava") {
            from(components["java"])
            groupId = libs.versions.maven.group.get()
            artifactId = "annotations"
            version = libs.versions.mod.version.get()
        }
    }
}