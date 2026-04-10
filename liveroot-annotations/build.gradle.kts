group = "net.autumn.liveroot"

repositories {
    mavenCentral()
}

mavenPublishing {
    coordinates("io.gitlab.tsuki-no-hikari", "liveroot-annotations", libs.versions.mod.version.get())

    pom {
        name.set("Liveroot")
        description.set("A Kotlin Symbol Processor(KSP) and annotation library providing a useful framework for Minecraft modding on the Fabric mod loader.")
        inceptionYear.set("2026")
        url.set("https://gitlab.com/tsuki-no-hikari/Liveroot")
        licenses {
            license {
                name.set("GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007")
                url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                distribution.set("https://www.gnu.org/licenses/gpl-3.0.txt")
            }
        }
        developers {
            developer {
                id.set("tsuki-no-hikari")
                name.set("Tsuki no Hikari")
                url.set("https://gitlab.com/tsuki-no-hikari")
            }
        }
        scm {
            url.set("https://gitlab.com/tsuki-no-hikari/Liveroot")
            connection.set("scm:git:git://gitlab.com/tsuki-no-hikari/Liveroot.git")
            developerConnection.set("scm:git:ssh://git@gitlab.com:tsuki-no-hikari/Liveroot.git")
        }
    }
}