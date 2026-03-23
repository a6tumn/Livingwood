@file:Suppress("unused")

package net.autumn.liveroot.type

import net.autumn.liveroot.registry.registerItem
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ToolMaterial

data class ToolSet(
    val nameSpaceAndPath: Identifier,
    val toolMaterial: ToolMaterial,
    val enabledItems: Set<ItemType> = ItemType.entries.toSet(),
    val speedPerTool: Map<ItemType, Float>,
    val damagePerTool: Map<ItemType, Float>,
    val spearProperties: List<Float> = emptyList(),
    val isFireResistant: Boolean = false
) {
    var sword: Item? = null
        private set
    var shovel: Item? = null
        private set
    var pickaxe: Item? = null
        private set
    var axe: Item? = null
        private set
    var hoe: Item? = null
        private set
    var spear: Item? = null
        private set

    var listOfItems: List<Item>
        private set

    init {
        validateEnabledToolMaps()

        var id: Identifier
        var properties: Item.Properties

        enabledItems.forEach { type ->
            when (type) {
                ItemType.SWORD -> {
                    id = nameSpaceAndPath.withSuffix("_sword")
                    properties = Item.Properties().sword(toolMaterial, damagePerTool.getValue(type), speedPerTool.getValue(type))
                    if (isFireResistant) {properties.fireResistant()}
                    sword = registerItem(id, ::Item, properties)
                }
                ItemType.SHOVEL -> {
                    id = nameSpaceAndPath.withSuffix("_shovel")
                    properties = Item.Properties().shovel(toolMaterial, damagePerTool.getValue(type), speedPerTool.getValue(type))
                    if (isFireResistant) {properties.fireResistant()}
                    shovel = registerItem(id, ::Item, properties)
                }
                ItemType.PICKAXE -> {
                    id = nameSpaceAndPath.withSuffix("_pickaxe")
                    properties = Item.Properties().pickaxe(toolMaterial, damagePerTool.getValue(type), speedPerTool.getValue(type))
                    if (isFireResistant) {properties.fireResistant()}
                    pickaxe = registerItem(id, ::Item, properties)
                }
                ItemType.AXE -> {
                    id = nameSpaceAndPath.withSuffix("_axe")
                    properties = Item.Properties().axe(toolMaterial, damagePerTool.getValue(type), speedPerTool.getValue(type))
                    if (isFireResistant) {properties.fireResistant()}
                    axe = registerItem(id, ::Item, properties)
                }
                ItemType.HOE -> {
                    id = nameSpaceAndPath.withSuffix("_hoe")
                    properties = Item.Properties().hoe(toolMaterial, damagePerTool.getValue(type), speedPerTool.getValue(type))
                    if (isFireResistant) {properties.fireResistant()}
                    hoe = registerItem(id, ::Item, properties)
                }
                ItemType.SPEAR -> {
                    id = nameSpaceAndPath.withSuffix("_spear")
                    properties = Item.Properties().spear(toolMaterial, spearProperties[0],spearProperties[1],spearProperties[2],spearProperties[3],spearProperties[4],spearProperties[5],spearProperties[6],spearProperties[7],spearProperties[8])
                    if (isFireResistant) {properties.fireResistant()}
                    spear = registerItem(id, ::Item, properties)
                }
            }
        }

        listOfItems = listOfNotNull(
            sword,
            shovel,
            pickaxe,
            axe,
            hoe,
            spear
        )
    }

    private fun validateEnabledToolMaps() {
        val missingDamage = enabledItems - damagePerTool.keys
        val missingSpeed = enabledItems - speedPerTool.keys

        if (ItemType.SPEAR in enabledItems) {
            require(spearProperties.size == 9) {
                "Missing properties for spear"
            }
        }

        require(missingDamage.isEmpty()) {
            "Missing damage values for: $missingDamage"
        }

        require(missingSpeed.isEmpty()) {
            "Missing speed values for: $missingSpeed"
        }
    }
}
