@file:Suppress("unused")

package net.autumn.liveroot.type

import net.autumn.liveroot.property.doorProperties
import net.autumn.liveroot.property.fenceGateProperties
import net.autumn.liveroot.property.fenceProperties
import net.autumn.liveroot.property.plankProperties
import net.autumn.liveroot.property.pressurePlateProperties
import net.autumn.liveroot.property.saplingProperties
import net.autumn.liveroot.property.shelfProperties
import net.autumn.liveroot.property.signProperties
import net.autumn.liveroot.property.trapdoorProperties
import net.autumn.liveroot.property.woodProperties
import net.autumn.liveroot.registry.registerBlock
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.CeilingHangingSignBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SaplingBlock
import net.minecraft.world.level.block.ShelfBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.TintedParticleLeavesBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallHangingSignBlock
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor

data class WoodSet(
    val namespaceAndPath: Identifier,
    val blockSetType: BlockSetType?,
    val woodType: WoodType?,
    val enabledBlocks: Set<BlockType> = BlockType.entries.toSet(),
    val treeGrower: TreeGrower?,
    val topMapColor: MapColor?,
    val sideMapColor: MapColor?
) {
    var log: Block? = null
        private set
    var strippedLog: Block? = null
        private set
    var wood: Block? = null
        private set
    var strippedWood: Block? = null
        private set
    var planks: Block? = null
        private set
    var leaves: Block? = null
        private set
    var sapling: Block? = null
        private set
    var pottedSapling: Block? = null
        private set
    var stairs: Block? = null
        private set
    var slab: Block? = null
        private set
    var door: Block? = null
        private set
    var trapdoor: Block? = null
        private set
    var fence: Block? = null
        private set
    var fenceGate: Block? = null
        private set
    var button: Block? = null
        private set
    var pressurePlate: Block? = null
        private set
    var shelf: Block? = null
        private set
    var sign: Block? = null
        private set
    var wallSign: Block? = null
        private set
    var hangingSign: Block? = null
        private set
    var wallHangingSign: Block? = null
        private set

    var listOfBlocks: List<Block>
    var exclusiveListOfBlocks: List<Block>

    init {
        validateEnabledBlocks()

        var id: Identifier
        var properties: BlockBehaviour.Properties

        enabledBlocks.forEach { type ->
            when (type) {
                BlockType.LOG -> {
                    require(topMapColor != null && sideMapColor != null) { "Cannot register log without a topMapColor and sideMapColor" }
                    id = namespaceAndPath.withSuffix("_log")
                    properties = Blocks.logProperties(topMapColor, sideMapColor, SoundType.WOOD)
                    log = registerBlock(id, ::RotatedPillarBlock, properties)
                }
                BlockType.STRIPPED_LOG -> {
                    require(topMapColor != null) { "Cannot register stripped log without a topMapColor" }
                    id = namespaceAndPath.withPrefix("stripped_").withSuffix("_log")
                    properties = Blocks.logProperties(topMapColor, topMapColor, SoundType.WOOD)
                    strippedLog = registerBlock(id, ::RotatedPillarBlock, properties)
                }
                BlockType.WOOD -> {
                    require(sideMapColor != null) { "Cannot register wood without a sideMapColor" }
                    id = namespaceAndPath.withSuffix("_wood")
                    properties = woodProperties(sideMapColor)
                    wood = registerBlock(id, ::RotatedPillarBlock, properties)
                }
                BlockType.STRIPPED_WOOD -> {
                    require(topMapColor != null) { "Cannot register stripped wood without a topMapColor" }
                    id = namespaceAndPath.withPrefix("stripped_").withSuffix("_wood")
                    properties = woodProperties(topMapColor)
                    strippedWood = registerBlock(id, ::RotatedPillarBlock, properties)
                }
                BlockType.PLANKS -> {
                    require(sideMapColor != null) { "Cannot register planks without a sideMapColor" }
                    id = namespaceAndPath.withSuffix("_planks")
                    properties = plankProperties(sideMapColor)
                    planks = registerBlock(id, ::Block, properties)
                }
                BlockType.LEAVES -> {
                    id = namespaceAndPath.withSuffix("_leaves")
                    properties = Blocks.leavesProperties(SoundType.GRASS)
                    leaves = registerBlock(id, { p -> TintedParticleLeavesBlock(0.01f, p) }, properties)
                }
                BlockType.SAPLING -> {
                    require(treeGrower != null) { "Cannot register sapling without a treeGrower" }
                    id = namespaceAndPath.withSuffix("_sapling")
                    properties = saplingProperties()
                    sapling = registerBlock(id, { p -> SaplingBlock(treeGrower, p) }, properties)
                }
                BlockType.POTTED_SAPLING -> {
                    require(sapling != null) { "Sapling should exist" }
                    id = namespaceAndPath.withPrefix("potted_").withSuffix("_sapling")
                    properties = Blocks.flowerPotProperties()
                    pottedSapling = registerBlock(id, { p -> FlowerPotBlock(sapling!!, p) }, properties)
                }
                BlockType.STAIRS -> {
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_stairs")
                    properties = BlockBehaviour.Properties.ofFullCopy(planks!!)
                    stairs = registerBlock(id, { p -> StairBlock(planks!!.defaultBlockState(), p) }, properties)
                }
                BlockType.SLAB -> {
                    require(sideMapColor != null) { "Cannot register slab without a sideMapColor" }
                    id = namespaceAndPath.withSuffix("_slab")
                    properties = plankProperties(sideMapColor)
                    slab = registerBlock(id, ::SlabBlock, properties)
                }
                BlockType.DOOR -> {
                    require(blockSetType != null) { "Cannot register door without a blockSetType" }
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_door")
                    properties = doorProperties(planks!!)
                    door = registerBlock(id, { p -> DoorBlock(blockSetType, p) }, properties)
                }
                BlockType.TRAPDOOR -> {
                    require(blockSetType != null) { "Cannot register trapdoor without a blockSetType" }
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_trapdoor")
                    properties = trapdoorProperties(planks!!)
                    trapdoor = registerBlock(id, { p -> TrapDoorBlock(blockSetType, p) }, properties)
                }
                BlockType.FENCE -> {
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_fence")
                    properties = fenceProperties(planks!!)
                    fence = registerBlock(id, ::FenceBlock, properties)
                }
                BlockType.FENCE_GATE -> {
                    require(woodType != null) { "Cannot register fence gate without a woodType" }
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_fence_gate")
                    properties = fenceGateProperties(planks!!)
                    fenceGate = registerBlock(id, { p -> FenceGateBlock(woodType, p) }, properties)
                }
                BlockType.BUTTON -> {
                    require(blockSetType != null) { "Cannot register button without a blockSetType" }
                    id = namespaceAndPath.withSuffix("_button")
                    properties = Blocks.buttonProperties()
                    button = registerBlock(id, { p -> ButtonBlock(blockSetType, 30, p) }, properties)
                }
                BlockType.PRESSURE_PLATE -> {
                    require(blockSetType != null) { "Cannot register pressure plate without a blockSetType" }
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_pressure_plate")
                    properties = pressurePlateProperties(planks!!)
                    pressurePlate = registerBlock(id, { p -> PressurePlateBlock(blockSetType, p) }, properties)
                }
                BlockType.SHELF -> {
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_shelf")
                    properties = shelfProperties(planks!!)
                    shelf = registerBlock(id, ::ShelfBlock, properties)
                }
                BlockType.SIGN -> {
                    require(woodType != null) { "Cannot register sign without a woodType" }
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_sign")
                    properties = signProperties(planks!!)
                    sign = registerBlock(id, { p -> StandingSignBlock(woodType, p) }, properties, false)
                    BlockEntityType.SIGN.addValidBlock(sign!!)
                }
                BlockType.WALL_SIGN -> {
                    require(woodType != null) { "Cannot register wall sign without a woodType" }
                    require(planks != null) { "Planks should exist" }
                    require(sign != null) { "Sign should exist" }
                    id = namespaceAndPath.withSuffix("_wall_sign")
                    properties = signProperties(planks!!).overrideLootTable(sign!!.lootTable).overrideDescription(sign!!.descriptionId)
                    wallSign = registerBlock(id, { p -> WallSignBlock(woodType, p) }, properties, false)
                    BlockEntityType.SIGN.addValidBlock(wallSign!!)
                }
                BlockType.HANGING_SIGN -> {
                    require(woodType != null) { "Cannot register hanging sign without a woodType" }
                    require(planks != null) { "Planks should exist" }
                    id = namespaceAndPath.withSuffix("_hanging_sign")
                    properties = signProperties(planks!!)
                    hangingSign = registerBlock(id, { p -> CeilingHangingSignBlock(woodType, p) }, properties, false)
                    BlockEntityType.HANGING_SIGN.addValidBlock(hangingSign!!)
                }
                BlockType.WALL_HANGING_SIGN -> {
                    require(woodType != null) { "Cannot register wall hanging sign without a woodType" }
                    require(planks != null) { "Planks should exist" }
                    require(hangingSign != null) { "Hanging sign should exist" }
                    id = namespaceAndPath.withSuffix("_wall_hanging_sign")
                    properties = signProperties(planks!!).overrideLootTable(hangingSign!!.lootTable).overrideDescription(hangingSign!!.descriptionId)
                    wallHangingSign = registerBlock(id, { p -> WallHangingSignBlock(woodType, p) }, properties, false)
                    BlockEntityType.HANGING_SIGN.addValidBlock(wallHangingSign!!)
                }
            }
        }

        listOfBlocks = listOfNotNull(
            log,
            strippedLog,
            wood,
            strippedWood,
            planks,
            leaves,
            sapling,
            pottedSapling,
            stairs,
            slab,
            door,
            trapdoor,
            fence,
            fenceGate,
            button,
            pressurePlate,
            shelf,
            sign,
            wallSign,
            hangingSign,
            wallHangingSign
        )
        exclusiveListOfBlocks = listOfNotNull(
            log,
            strippedLog,
            wood,
            strippedWood,
            planks,
            leaves,
            sapling,
            stairs,
            slab,
            door,
            trapdoor,
            fence,
            fenceGate,
            button,
            pressurePlate,
            shelf
        )
    }

    private fun validateEnabledBlocks() {
        val requiredBlocks: Map<BlockType, Set<BlockType>> = mapOf(
            BlockType.POTTED_SAPLING to setOf(BlockType.SAPLING),
            BlockType.STAIRS to setOf(BlockType.PLANKS),
            BlockType.TRAPDOOR to setOf(BlockType.PLANKS),
            BlockType.DOOR to setOf(BlockType.PLANKS),
            BlockType.FENCE to setOf(BlockType.PLANKS),
            BlockType.FENCE_GATE to setOf(BlockType.PLANKS),
            BlockType.BUTTON to setOf(BlockType.PLANKS),
            BlockType.SHELF to setOf(BlockType.PLANKS),
            BlockType.SIGN to setOf(BlockType.PLANKS),
            BlockType.WALL_SIGN to setOf(BlockType.PLANKS, BlockType.SIGN),
            BlockType.HANGING_SIGN to setOf(BlockType.PLANKS),
            BlockType.WALL_HANGING_SIGN to setOf(BlockType.PLANKS, BlockType.HANGING_SIGN),
        )

        requiredBlocks.forEach { (block, dependencies) ->
            if (block in enabledBlocks) {
                dependencies.forEach { dep ->
                    require(dep in enabledBlocks) {
                        "Cannot enable $block without enabling $dep"
                    }
                }
            }
        }
    }
}
