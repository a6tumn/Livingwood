![liveroot](docs/title.png)

---

## Licensing

- All code is released under the **GNU GPL v3.0** license [HERE](docs/legal/LICENSE).
- All non-code assets are licensed under **CC BY-NC-SA 4.0 International** [HERE](docs/legal/LICENSE.assets).

See the `LICENSE` file(s) for full details.

---

## Acknowledgements

Credit for the icon asset goes to:

- [TeamTwilight](https://github.com/TeamTwilight)
- [Benimatic](https://github.com/Benimatic)

Be sure to check out their project.

---

## Versions

- Latest Mod Version - `0.1.1`
- Supported Minecraft Version(s) - `26.2`
- Supported Kotlin Version - `2.3.20`

---

## Annotations

- An explanation of each annotation can be found [HERE](docs/annotations.md).

---

## Getting Started

- Artifacts will be posted to Maven Central, make sure to add the repository to your project sources.

### Common
#### *libs.versions.toml*
```toml
[versions]
ksp = "2.3.5"
liveroot = "0.1.1"

[libraries]
liveroot = { module = "io.gitlab.tsuki-no-hikari:liveroot-annotations", version.ref = "liveroot"}
livingwood = { module = "io.gitlab.tsuki-no-hikari:livingwood-processor", version.ref = "liveroot"}

[plugins]
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

### Kotlin DSL
#### *build.gradle.kts*
```kotlin
plugins {
    alias(libs.plugins.ksp)
}

// ...

dependencies {
    implementation(libs.liveroot)
    ksp(libs.livingwood)
}
```

### Groovy DSL
#### *build.gradle*
```groovy
plugins {
    id 'com.google.devtools.ksp' version libs.versions.ksp.get()
}

// ...

dependencies {
    implementation libs.liveroot
    ksp libs.livingwood
}
```

---