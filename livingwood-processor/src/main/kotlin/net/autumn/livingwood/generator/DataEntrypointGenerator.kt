package net.autumn.livingwood.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import net.autumn.livingwood.aggregator.ImportAggregator
import net.autumn.livingwood.resolver.ImportResolver
import net.autumn.livingwood.util.enums.EntrypointType
import net.autumn.livingwood.util.writeDataEntrypoint

class DataEntrypointGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val packageName: String = "net.autumn.livingwood.generated"
) {
    fun generate(
        classes: List<KSClassDeclaration>
    ) {
        if (classes.isEmpty()) return

        val validFiles = classes.mapNotNull { it.containingFile }
            .toTypedArray()
            .takeIf { it.isNotEmpty() } ?: return

        val file = codeGenerator.createNewFile(
            Dependencies(aggregating = false, *validFiles),
            packageName,
            EntrypointType.DATA.generatedClassName
        )

        val imports = ImportAggregator().apply {
            add("net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint")
            add("net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator")
            add("net.minecraft.core.RegistrySetBuilder")
            add("net.minecraft.core.registries.Registries")
            add("io.autumn.twilight.registry.TwilightRegistries")
            classes.forEach { add(ImportResolver.resolve(it)) }
        }

        writeDataEntrypoint(
            file,
            packageName,
            imports,
            classes,
            logger
        )
    }
}