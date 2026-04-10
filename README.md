## Licensing

- All code is released under the **GNU GPL v3.0** license [HERE](LICENSE).
- All non-code assets are licensed under **CC BY-NC-SA 4.0 International** [HERE](LICENSE.assets).

See the `LICENSE` file(s) for full details.

---

## Acknowledgements

Credit for the icon asset goes to:

- [TeamTwilight](https://github.com/TeamTwilight)
- [Benimatic](https://github.com/Benimatic)

Be sure to check out their project.

---

## Current Supported Version

- `26.2-snapshot-2`

---

## Annotations

- *@MainEntrypoint*
- Can be used to automatically call functions from a generated entrypoint file.
```kotlin
@MainEntrypoint
fun initialize() {
    ModId.LOGGER.info("Registering example for ${ModId.NAMESPACE}")
}
```
---

- *@ClientEntrypoint*
- Can be used to automatically call functions from a generated client entrypoint file.
```kotlin
@ClientEntrypoint
fun initializeClient() {
    ModId.LOGGER.info("Registering example for ${ModId.NAMESPACE}")
}
```
---

- *@DataEntrypoint*
- Can be used on classes to automatically add data providers or on objects to build dynamic registries.
```kotlin
@DataEntrypoint(DataEntrypointType.DATA_PROVIDER)
class AdvancementProvider(
  output: FabricPackOutput,
  registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricAdvancementProvider(output, registriesFuture) {
  override fun generateAdvancement(
    registryLookup: HolderLookup.Provider,
    consumer: Consumer<AdvancementHolder>
  ) {
      TODO("...")
  }
}
```
```kotlin
@DataEntrypoint(DataEntrypointType.DYNAMIC_REGISTRY, Registries::class, "BIOME")
object Biome {
  fun bootstrap(
    context: BootstrapContext<Biome>
  ) {
    TODO("...")
  }
}
```
---

- *@Growth*
- Can be used to generate lists of object fields in a centralized registry lookup.
```kotlin
@Growth
object Items {
  val EXAMPLE_ITEM: Item = TODO("...")
}
```
---

- *@Rot*
- Can be used on object fields to exclude them from being added to the centralized registry lookup.
```kotlin
@Growth
object Items {
    @Rot
    val EXAMPLE_ITEM: Item = TODO("...")
}
```
```kotlin
@Growth("tagKey")
enum class BlockTags(
  val tagKey: TagKey<Block>
) {
    @Rot
    EXAMPLE_TAG(TODO("..."))
}
```