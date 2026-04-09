group = "net.autumn.liveroot"

repositories {
    mavenCentral()
}

publishing {
    publications {
        register<MavenPublication>("MavenJava") {
            from(components["java"])
            groupId = group.toString()
            artifactId = "annotations"
            version = libs.versions.mod.version.get()
        }
    }
}