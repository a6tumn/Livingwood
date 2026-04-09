group = "net.autumn.livingwood"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.3.5")
}

publishing {
    publications {
        register<MavenPublication>("MavenJava") {
            from(components["java"])
            groupId = group.toString()
            artifactId = "processor"
            version = libs.versions.mod.version.get()
        }
    }
}