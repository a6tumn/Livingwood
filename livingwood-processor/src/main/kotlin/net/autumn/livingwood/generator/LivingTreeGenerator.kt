package net.autumn.livingwood.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import net.autumn.livingwood.aggregator.ImportAggregator
import net.autumn.livingwood.resolver.ImportResolver
import net.autumn.livingwood.util.enums.AnnotationType
import net.autumn.livingwood.util.enums.GeneratedClassName
import net.autumn.livingwood.util.writeLivingTree

class LivingTreeGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val packageName: String = "net.autumn.livingwood.generated"
) {
    fun generate(
        classes: List<KSClassDeclaration>
    ) {
        if (classes.isEmpty()) return

        val imports = ImportAggregator()
        val grouped = mutableMapOf<String, MutableList<String>>()

        classes.forEach { clazz ->
            ImportResolver.resolve(clazz)?.let { imports.add(it) }

            val propertyName = clazz.annotations
                .firstOrNull()
                ?.arguments
                ?.firstOrNull { it.name?.asString() == "property" }
                ?.value as? String

            when (clazz.classKind) {
                ClassKind.OBJECT -> {
                    if (propertyName.isNullOrBlank()) {
                        logger.error(
                            "@Growth on '${clazz.simpleName.asString()}' must define a valid 'property'.",
                            clazz
                        )
                        return@forEach
                    }
                }
                else -> {
                    logger.error("@Growth can only be applied to objects.", clazz)
                    return@forEach
                }
            }

            val entries = mutableListOf<String>()

            clazz.getAllProperties().forEach { prop ->
                if (prop.rotten()) {
                    logger.info("Skipping property '${prop.simpleName.asString()}' due to @Rot.")
                    return@forEach
                }

                val name = prop.simpleName.asString()
                entries += "${clazz.simpleName.asString()}.$name"
            }

            if (entries.isNotEmpty()) {
                grouped.getOrPut(propertyName) { mutableListOf() }
                    .addAll(entries)
            }
        }

        if (grouped.isEmpty()) return

        val validFiles = classes.mapNotNull { it.containingFile }
            .toTypedArray()
            .takeIf { it.isNotEmpty() } ?: return

        val file = codeGenerator.createNewFile(
            Dependencies(aggregating = false, *validFiles),
            packageName,
            GeneratedClassName.LIVING_TREE.simpleName
        )

        val lines = mutableListOf<String>()

        grouped.toSortedMap().forEach { (propertyName, entries) ->
            val listName = "listOf${propertyName.replaceFirstChar { it.uppercase() }}"
            lines += "val $listName = listOf("
            entries.forEachIndexed { index, entry ->
                val comma = if (index != entries.lastIndex) "," else ""
                lines += "    $entry$comma"
            }
            lines += ")"
            lines += ""
        }

        writeLivingTree(
            file,
            packageName,
            imports,
            lines
        )
    }

    private fun KSAnnotated.rotten(): Boolean =
        annotations.any {
            it.annotationType.resolve().declaration.qualifiedName?.asString() ==
                    AnnotationType.Rot.qualifiedName
        }
}