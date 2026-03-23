package net.autumn.livingwood.util.enums

enum class FabricInterface(
    val qualifiedName: String
) {
    MAIN("net.fabricmc.api.ModInitializer"),
    CLIENT("net.fabricmc.api.ClientModInitializer"),
    DATA("net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint");
}