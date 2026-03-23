@file:Suppress("unused")

package net.autumn.liveroot.registry

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType

fun registerBlock(
    namespaceAndPath: Identifier,
    factory: (BlockBehaviour.Properties) -> Block = ::Block,
    settings: BlockBehaviour.Properties = BlockBehaviour.Properties.of(),
    shouldRegisterItem: Boolean = true
) : Block {
    val blockKey = keyOfAny(namespaceAndPath, Registries.BLOCK)
    val block = factory(settings.setId(blockKey))
    if (shouldRegisterItem) {
        val itemKey = keyOfAny(namespaceAndPath, Registries.ITEM)
        val item = BlockItem(block, Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
        Registry.register(
            BuiltInRegistries.ITEM,
            itemKey,
            item
        )
    }
    return Registry.register(
        BuiltInRegistries.BLOCK,
        blockKey,
        block
    )
}

fun registerItem(
    namespaceAndPath: Identifier,
    factory: (Item.Properties) -> Item = ::Item,
    settings: Item.Properties = Item.Properties()
): Item {
    val itemKey = keyOfAny(namespaceAndPath, Registries.ITEM)
    val item = factory(settings.setId(itemKey))
    return Registry.register(
        BuiltInRegistries.ITEM,
        itemKey,
        item
    )
}

fun registerCreativeModeTab(
    namespaceAndPath: Identifier,
    icon: ItemLike,
    vararg entriesLists: List<ItemLike>
): CreativeModeTab {
    val allEntries = entriesLists.asIterable().flatten()
    val path = namespaceAndPath.path
    return Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ResourceKey.create(Registries.CREATIVE_MODE_TAB, namespaceAndPath),
        FabricCreativeModeTab.builder()
            .title(Component.literal(path.replaceFirstChar { it.uppercase() }))
            .icon { ItemStack(icon) }
            .displayItems { _, output -> allEntries.forEach(output::accept) }
            .build()
    )
}

fun <T : BlockEntity> registerBlockEntityType(
    namespaceAndPath: Identifier,
    factory: (BlockPos, BlockState) -> T,
    vararg blocks: Block
): BlockEntityType<T> =
    Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        namespaceAndPath,
        FabricBlockEntityTypeBuilder.create(factory, *blocks).build()
    )

fun registerFoliagePlacerType(
    name: String,
    codec: MapCodec<out FoliagePlacer>
): FoliagePlacerType<out FoliagePlacer> =
    Registry.register(
        BuiltInRegistries.FOLIAGE_PLACER_TYPE,
        name,
        FoliagePlacerType(codec)
    )

fun registerTreeDecoratorType(
    name: String,
    codec: MapCodec<out TreeDecorator>
): TreeDecoratorType<out TreeDecorator> =
    Registry.register(
        BuiltInRegistries.TREE_DECORATOR_TYPE,
        name,
        TreeDecoratorType(codec)
    )

fun registerTrunkPlacerType(
    name: String,
    codec: MapCodec<out TrunkPlacer>
): TrunkPlacerType<out TrunkPlacer> =
    Registry.register(
        BuiltInRegistries.TRUNK_PLACER_TYPE,
        name,
        TrunkPlacerType(codec)
    )

fun <T : ParticleOptions>registerParticleType(
    namespaceAndPath: Identifier,
    particleType: ParticleType<T>
): ParticleType<T> =
    Registry.register(
        BuiltInRegistries.PARTICLE_TYPE,
        namespaceAndPath,
        particleType
    )

fun registerSoundEvent(
    namespaceAndPath: Identifier
): SoundEvent =
    Registry.register(
        BuiltInRegistries.SOUND_EVENT,
        namespaceAndPath,
        SoundEvent.createVariableRangeEvent(namespaceAndPath)
    )

private fun <T : Any>keyOfAny(
    namespaceAndPath: Identifier,
    registryName: ResourceKey<out Registry<T>>
): ResourceKey<T> {
    return ResourceKey.create(registryName, namespaceAndPath)
}