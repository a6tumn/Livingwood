package net.autumn.livingwood.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import net.autumn.livingwood.aggregator.ImportAggregator
import net.autumn.livingwood.aggregator.NameAggregator
import net.autumn.livingwood.resolver.ImportResolver
import net.autumn.livingwood.resolver.NameResolver
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
        val lines = mutableListOf<String>()
        val nameAggregator = NameAggregator()

        classes.forEach { clazz ->
            ImportResolver.resolve(clazz)?.let { imports.add(it) }

            val propertyName = clazz.annotations
                .firstOrNull()
                ?.arguments
                ?.firstOrNull { it.name?.asString() == "property" }
                ?.value as? String

            when (clazz.classKind) {
                ClassKind.ENUM_CLASS -> {
                    if (propertyName.isNullOrBlank() || propertyName == "__UNSET__") {
                        logger.error(
                            "@Growth on enum '${clazz.simpleName.asString()}' must define a valid 'property'",
                            clazz
                        )
                        return@forEach
                    }
                    handleEnum(clazz, clazz.simpleName.asString(), propertyName, lines, nameAggregator)
                }

                ClassKind.OBJECT -> handleObject(clazz, clazz.simpleName.asString(), lines, nameAggregator)
                else -> logger.error("@Growth can only be applied to enum classes or objects", clazz)
            }
        }

        if (lines.isEmpty()) return

        val validFiles = classes.mapNotNull { it.containingFile }
            .toTypedArray()
            .takeIf { it.isNotEmpty() } ?: return

        val file = codeGenerator.createNewFile(
            Dependencies(aggregating = false, *validFiles),
            packageName,
            GeneratedClassName.LIVING_TREE.simpleName
        )

        writeLivingTree(
            file,
            packageName,
            imports,
            lines
        )
    }

    private fun handleEnum(
        clazz: KSClassDeclaration,
        className: String,
        propertyName: String,
        lines: MutableList<String>,
        aggregator: NameAggregator
    ) {
        val property = clazz.getAllProperties().firstOrNull { it.simpleName.asString() == propertyName }
        if (property == null) {
            logger.error("Property '$propertyName' not found in enum '$className'", clazz)
            return
        }

        clazz.declarations.filterIsInstance<KSClassDeclaration>().filter { it.classKind != ClassKind.OBJECT || it.simpleName.asString() != "Companion" }.forEach { entry ->
            if (entry.rotten()) {
                logger.info("Skipping enum constant '${entry.simpleName.asString()}' due to @Rot")
                return@forEach
            }

            val entryName = entry.simpleName.asString()
            val baseName = NameResolver.resolve(entryName, className)
            val finalName = aggregator.add(baseName, className)
            lines += "val $finalName by lazy { $className.$entryName.$propertyName }"
        }
    }

    private fun handleObject(
        clazz: KSClassDeclaration,
        className: String,
        lines: MutableList<String>,
        aggregator: NameAggregator
    ) {
        clazz.getAllProperties().forEach { prop ->
            if (prop.rotten()) {
                logger.info("Skipping property '${prop.simpleName.asString()}' due to @Rot")
                return@forEach
            }

            val name = prop.simpleName.asString()
            val baseName = NameResolver.resolve(name, className)
            val finalName = aggregator.add(baseName, className)
            lines += "val $finalName by lazy { $className.$name }"
        }
    }

    private fun KSAnnotated.rotten(): Boolean {
        return annotations.any {
            it.annotationType.resolve().declaration.qualifiedName?.asString() == AnnotationType.Rot.qualifiedName
        }
    }
}