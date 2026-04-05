package net.autumn.livingwood.util

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import net.autumn.livingwood.aggregator.ImportAggregator
import net.autumn.livingwood.util.enums.EntrypointType
import java.io.OutputStream

fun writeMainEntrypoint(
    file: OutputStream,
    packageName: String,
    imports: ImportAggregator,
    calls: List<String>
) {
    file.bufferedWriter().use { writer ->
        writer.appendLine("package $packageName\n")
        imports.writeTo(writer)
        writer.appendLine()
        writer.appendLine(
            "class ${EntrypointType.MAIN.generatedClassName} : " +
                    "${EntrypointType.MAIN.fabricInterface.substringAfterLast(".")} {"
        )
        writer.appendLine("    override fun onInitialize() {")
        calls.forEach { call ->
            val fqcn = call.substringBefore("(").substringBeforeLast(".")
            val shortName = fqcn.substringAfterLast(".")
            val callWithShortName = call.replace(fqcn, shortName)
            writer.appendLine("        $callWithShortName")
        }
        writer.appendLine("    }")
        writer.appendLine("}")
    }
}

fun writeClientEntrypoint(
    file: OutputStream,
    packageName: String,
    imports: ImportAggregator,
    calls: List<String>
) {
    file.bufferedWriter().use { writer ->
        writer.appendLine("package $packageName\n")
        imports.writeTo(writer)
        writer.appendLine()
        writer.appendLine(
            "class ${EntrypointType.CLIENT.generatedClassName} : " +
                    "${EntrypointType.CLIENT.fabricInterface.substringAfterLast(".")} {"
        )
        writer.appendLine("    override fun onInitializeClient() {")
        calls.forEach { call ->
            val fqcn = call.substringBefore("(").substringBeforeLast(".")
            val shortName = fqcn.substringAfterLast(".")
            val callWithShortName = call.replace(fqcn, shortName)
            writer.appendLine("        $callWithShortName")
        }
        writer.appendLine("    }")
        writer.appendLine("}")
    }
}

fun writeDataEntrypoint(
    file: OutputStream,
    packageName: String,
    imports: ImportAggregator,
    classes: List<KSClassDeclaration>,
    logger: KSPLogger
) {
    file.bufferedWriter().use { writer ->
        writer.appendLine("package $packageName\n")
        imports.writeTo(writer)
        writer.appendLine()
        writer.appendLine("class ${EntrypointType.DATA.generatedClassName} : DataGeneratorEntrypoint {")
        writer.appendLine("    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {")
        writer.appendLine("        val pack = fabricDataGenerator.createPack()")
        writer.appendLine()
        classes.forEach { clazz ->
            val type = clazz.annotations.first().getArgument("type")?.toString()
            if (type == "DATA_PROVIDER") {
                writer.appendLine("        pack.addProvider(::${clazz.simpleName.asString()})")
            }
        }
        writer.appendLine("    }")
        writer.appendLine()
        writer.appendLine("    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {")
        classes.forEach { clazz ->
            val annotation = clazz.annotations.first()
            val type = annotation.getArgument("type")?.toString()
            if (type == "DYNAMIC_REGISTRY") {
                val key = annotation.getArgument("key")?.toString()
                    ?: run {
                        logger.error("@DataEntrypoint with type DYNAMIC_REGISTRY requires a key", clazz)
                        return@forEach
                    }
                writer.appendLine(
                    "        registryBuilder.add(Registries.${key.uppercase()}, ${clazz.simpleName.asString()}::bootstrap)"
                )
            }
        }
        writer.appendLine("    }")
        writer.appendLine("}")
    }
}

fun writeLivingTree(
    file: OutputStream,
    packageName: String,
    imports: ImportAggregator,
    lines: List<String>
) {
    file.bufferedWriter().use { writer ->
        writer.appendLine("package $packageName")
        writer.appendLine()
        imports.writeTo(writer)
        writer.appendLine()
        writer.appendLine("object LivingTree {")
        writer.appendLine()
        lines.forEach { block ->
            block.lines().forEach { line ->
                writer.appendLine("    $line")
            }
        }
        writer.appendLine("}")
    }
}