@file:Suppress("unused")

package net.autumn.liveroot.datagen

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators.PlantType
import net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock
import net.minecraft.client.data.models.BlockModelGenerators.plainVariant
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import kotlin.collections.forEach

fun TranslationBuilder.addBlocks(blocks: List<Block>) {
    blocks.forEach { block ->
        add(block, toLangCase(BuiltInRegistries.BLOCK.getKey(block).path))
    }
}

fun TranslationBuilder.addItems(items: List<Item>) {
    items.forEach { item ->
        add(item, toLangCase(BuiltInRegistries.ITEM.getKey(item).path))
    }
}

fun TranslationBuilder.addItemTags(itemTags: List<TagKey<Item>>) {
    itemTags.forEach { itemTag ->
        add(itemTag, toLangCase(itemTag.translationKey))
    }
}

fun ItemModelGenerators.createFlatItemModels(itemList: List<Item>) {
    for (item in itemList) {
        generateFlatItem(item, ModelTemplates.FLAT_ITEM)
    }
}

fun ItemModelGenerators.createFlatHandheldItemModels(itemList: List<Item>) {
    for (item in itemList) {
        generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
    }
}

fun BlockModelGenerators.createPlantWithAltPotted(name: String, standAlone: Block, potted: Block, pottedTexture: Identifier, plantType: PlantType) {
    registerSimpleItemModel(
        standAlone.asItem(),
        plantType.createItemModel(this, standAlone)
    )
    createPlant(name, standAlone, potted, pottedTexture, plantType)
}



private fun BlockModelGenerators.createPlant(name: String, standAlone: Block, potted: Block, pottedTexture: Identifier, plantType: PlantType) {
    createCrossBlock(standAlone, plantType)

    val textures = plantType.getPlantTextureMapping(standAlone)

    val pottedTexture = Material(pottedTexture)

    textures.put(TextureSlot.PLANT, pottedTexture)

    val model = plainVariant(
        plantType.crossPot.create(potted, textures, modelOutput)
    )

    blockStateOutput.accept(createSimpleBlock(potted, model))
}

private fun toLangCase(name: String): String {
    return name
        .substringAfterLast('.')
        .replace('_', ' ')
        .split(' ')
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
}