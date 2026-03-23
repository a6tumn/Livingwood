@file:Suppress("unused")

package net.autumn.liveroot.registry.client

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

fun <T : ParticleOptions> registerParticleProvider(
    particleType: ParticleType<T>,
    pendingParticleProvider: ParticleProviderRegistry.PendingParticleProvider<T>
) {
    ParticleProviderRegistry.getInstance().register(particleType, pendingParticleProvider)
}

fun registerBlockColor(
    layers: List<BlockTintSource>,
    vararg blocks: Block
) {
    BlockColorRegistry.register(layers, *blocks)
}

fun <T : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(
    type: BlockEntityType<T>,
    factory: (BlockEntityRendererProvider.Context) -> BlockEntityRenderer<T, S>
) {
    BlockEntityRenderers.register(type) { context ->
        factory(context)
    }
}