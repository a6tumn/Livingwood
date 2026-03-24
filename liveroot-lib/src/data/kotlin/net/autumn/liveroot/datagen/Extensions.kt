@file:Suppress("unused")

package net.autumn.liveroot.datagen

import net.autumn.liveroot.type.ToolSet
import net.autumn.liveroot.type.WoodSet
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators.PlantType
import net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock
import net.minecraft.client.data.models.BlockModelGenerators.plainVariant
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.BlockFamily
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.TintedParticleLeavesBlock
import kotlin.collections.forEach

fun TranslationBuilder.addBlocks(
    blocks: List<Block>
) {
    blocks.forEach { block ->
        add(block, toLangCase(BuiltInRegistries.BLOCK.getKey(block).path))
    }
}

fun TranslationBuilder.addItems(
    items: List<Item>
) {
    items.forEach { item ->
        add(item, toLangCase(BuiltInRegistries.ITEM.getKey(item).path))
    }
}

fun TranslationBuilder.addItemTags(
    itemTags: List<TagKey<Item>>
) {
    itemTags.forEach { itemTag ->
        add(itemTag, toLangCase(itemTag.translationKey))
    }
}

fun TranslationBuilder.addWoodSet(
    woodSet: WoodSet
) {
    val woodName = woodSet.namespaceAndPath.path
    fun addIfPresent(block: Block?, suffix: String, prefix: String = "") {
        if (block != null && woodSet.listOfBlocks.contains(block)) {
            add(block, toLangCase("${woodName}_$suffix"))
        }
    }

    addIfPresent(woodSet.log, "Log")
    addIfPresent(woodSet.strippedLog, "Log", "Stripped")
    addIfPresent(woodSet.wood, "Wood")
    addIfPresent(woodSet.strippedWood, "Wood", "Stripped")
    addIfPresent(woodSet.leaves, "Leaves")
    addIfPresent(woodSet.sapling, "Sapling")
    addIfPresent(woodSet.planks, "Planks")
    addIfPresent(woodSet.door, "Door")
    addIfPresent(woodSet.trapdoor, "Trapdoor")
    addIfPresent(woodSet.fence, "Fence")
    addIfPresent(woodSet.fenceGate, "Fence Gate")
    addIfPresent(woodSet.stairs, "Stairs")
    addIfPresent(woodSet.slab, "Slab")
    addIfPresent(woodSet.button, "Button")
    addIfPresent(woodSet.pressurePlate, "Pressure Plate")
    addIfPresent(woodSet.shelf, "Shelf")
    addIfPresent(woodSet.sign, "Sign")
    addIfPresent(woodSet.hangingSign, "Hanging Sign")
}

fun TranslationBuilder.addToolSet(
    toolSet: ToolSet
) {
    val setName = toolSet.nameSpaceAndPath.path.replaceFirstChar { it.uppercase() }
    fun addIfPresent(item: Item?, name: String) {
        if (item != null && toolSet.listOfItems.contains(item)) {
            add(item, name)
        }
    }

    addIfPresent(toolSet.sword, "$setName Sword")
    addIfPresent(toolSet.shovel, "$setName Shovel")
    addIfPresent(toolSet.pickaxe, "$setName Pickaxe")
    addIfPresent(toolSet.axe, "$setName Axe")
    addIfPresent(toolSet.hoe, "$setName Hoe")
    addIfPresent(toolSet.spear, "$setName Spear")
}

fun ItemModelGenerators.createFlatItemModels(
    itemList: List<Item>
) {
    for (item in itemList) {
        generateFlatItem(item, ModelTemplates.FLAT_ITEM)
    }
}

fun ItemModelGenerators.createFlatHandheldItemModels(
    itemList: List<Item>
) {
    for (item in itemList) {
        generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
    }
}

fun ItemModelGenerators.createToolSetModels(
    toolSet: ToolSet
) {
    for(item in toolSet.listOfItems) {
        generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
    }
}

fun BlockModelGenerators.createPlantWithAltPotted(
    name: String,
    standAlone: Block,
    potted: Block,
    pottedTexture: Identifier,
    plantType: PlantType
) {
    registerSimpleItemModel(
        standAlone.asItem(),
        plantType.createItemModel(this, standAlone)
    )
    createPlant(name, standAlone, potted, pottedTexture, plantType)
}

private fun BlockModelGenerators.createPlant(
    name: String,
    standAlone: Block,
    potted: Block,
    pottedTexture: Identifier,
    plantType: PlantType
) {
    createCrossBlock(standAlone, plantType)

    val textures = plantType.getPlantTextureMapping(standAlone)

    val pottedTexture = Material(pottedTexture)

    textures.put(TextureSlot.PLANT, pottedTexture)

    val model = plainVariant(
        plantType.crossPot.create(potted, textures, modelOutput)
    )

    blockStateOutput.accept(createSimpleBlock(potted, model))
}

fun BlockModelGenerators.createWoodSetModels(
    woodSet: WoodSet,
    blockFamily: BlockFamily,
    leavesTintColor: Int
) {
    fun has(block: Block?) = block != null && woodSet.listOfBlocks.contains(block)
    fun hasAll(vararg blocks: Block?) = blocks.all { it != null && woodSet.listOfBlocks.contains(it) }

    if (hasAll(woodSet.log, woodSet.wood)) {
        woodProvider(woodSet.log!!).logWithHorizontal(woodSet.log!!).wood(woodSet.wood!!)
    }
    if (hasAll(woodSet.strippedLog, woodSet.strippedWood)) {
        woodProvider(woodSet.strippedLog!!).logWithHorizontal(woodSet.strippedLog!!).wood(woodSet.strippedWood!!)
    }
    if (has(woodSet.leaves)) {
        createTintedLeaves(woodSet.leaves!!, TexturedModel.LEAVES, leavesTintColor)
    }
    if (hasAll(woodSet.sapling, woodSet.pottedSapling)) {
        createPlantWithDefaultItem(woodSet.sapling!!, woodSet.pottedSapling!!, PlantType.NOT_TINTED)
    }
    if (hasAll(woodSet.strippedLog, woodSet.hangingSign, woodSet.wallHangingSign)) {
        createHangingSign(woodSet.strippedLog!!, woodSet.hangingSign!!, woodSet.wallHangingSign!!)
    }
    family(woodSet.planks!!).generateFor(blockFamily)
}

fun BlockLootSubProvider.createWoodSetDrops(
    woodSet: WoodSet
) {
    for (block in woodSet.listOfBlocks) {
        when (block) {
            is TintedParticleLeavesBlock -> {
                require(woodSet.sapling != null) { "Sapling should exist" }
                createLeavesDrops(block, woodSet.sapling!!, 0.05f)
            }
            else -> dropSelf(block)
        }
    }
}

fun RecipeProvider.createWoodSetRecipes(
    woodSet: WoodSet,
    blockFamily: BlockFamily,
    logTag: TagKey<Item>
) {
    fun has(block: Block?) = block != null && woodSet.listOfBlocks.contains(block)
    fun hasAll(vararg blocks: Block?) = blocks.all { it != null && woodSet.listOfBlocks.contains(it) }

    if (has(woodSet.planks)) {
        planksFromLog(woodSet.planks!!, logTag, 4)
    }
    if(hasAll(woodSet.wood, woodSet.log)) {
        woodFromLogs(woodSet.wood!!, woodSet.log!!)
    }
    if(hasAll(woodSet.strippedWood, woodSet.strippedLog)) {
        woodFromLogs(woodSet.strippedWood!!, woodSet.strippedLog!!)
    }
    generateRecipes(blockFamily, FeatureFlags.REGISTRY.allFlags())
}

fun RecipeProvider.createToolSetRecipes(toolSet: ToolSet, materialTag: TagKey<Item>, output: RecipeOutput) {
    val unlockName = "has_${toolSet.nameSpaceAndPath.path.replaceFirstChar { it.lowercase() }}"
    if(toolSet.listOfItems.contains(toolSet.sword)) {
        shaped(RecipeCategory.COMBAT, toolSet.sword!!)
            .pattern("X")
            .pattern("X")
            .pattern("#")
            .define('#', Items.STICK)
            .define('X', materialTag)
            .unlockedBy(unlockName, this.has(materialTag))
            .save(output)
    }
    if(toolSet.listOfItems.contains(toolSet.shovel)) {
        shaped(RecipeCategory.TOOLS, toolSet.shovel!!)
            .pattern("X")
            .pattern("#")
            .pattern("#")
            .define('#', Items.STICK)
            .define('X', materialTag)
            .unlockedBy(unlockName, this.has(materialTag))
            .save(output)
    }
    if(toolSet.listOfItems.contains(toolSet.pickaxe)) {
        shaped(RecipeCategory.TOOLS,toolSet.pickaxe!!)
            .pattern("XXX")
            .pattern(" # ")
            .pattern(" # ")
            .define('#', Items.STICK)
            .define('X', materialTag)
            .unlockedBy(unlockName, this.has(materialTag))
            .save(output)
    }
    if(toolSet.listOfItems.contains(toolSet.axe)) {
        shaped(RecipeCategory.TOOLS, toolSet.axe!!)
            .pattern("XX")
            .pattern("X#")
            .pattern(" #")
            .define('#', Items.STICK)
            .define('X', materialTag)
            .unlockedBy(unlockName, this.has(materialTag))
            .save(output)
    }
    if(toolSet.listOfItems.contains(toolSet.hoe)) {
        shaped(RecipeCategory.TOOLS, toolSet.hoe!!)
            .pattern("XX")
            .pattern(" #")
            .pattern(" #")
            .define('#', Items.STICK)
            .define('X', materialTag)
            .unlockedBy(unlockName, this.has(materialTag))
            .save(output)
    }
    if(toolSet.listOfItems.contains(toolSet.spear)) {
        shaped(RecipeCategory.TOOLS, toolSet.spear!!)
            .pattern("  X")
            .pattern(" #")
            .pattern("#")
            .define('#', Items.STICK)
            .define('X', materialTag)
            .unlockedBy(unlockName, this.has(materialTag))
            .save(output)
    }
}

private fun toLangCase(
    name: String
): String =
    name.
    substringAfterLast('.').
    replace('_', ' ').
    split(' ').
    joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }