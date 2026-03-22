package net.autumn.livingwood.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import net.autumn.livingwood.aggregator.ImportAggregator
import net.autumn.livingwood.resolver.FunctionCallResolver
import net.autumn.livingwood.util.enums.AnnotationType
import net.autumn.livingwood.util.enums.EntrypointType
import net.autumn.livingwood.util.writeMainEntrypoint

class MainEntrypointGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val packageName: String = "net.autumn.livingwood.generated"
) {
    fun generate(
        functions: List<KSFunctionDeclaration>
    ) {
        if (functions.isEmpty()) return

        val validFiles = functions.mapNotNull { it.containingFile }.toTypedArray()
        if (validFiles.isEmpty()) return

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false, *validFiles),
            packageName = packageName,
            fileName = EntrypointType.MAIN.generatedClassName
        )

        val imports = ImportAggregator()
        imports.add(EntrypointType.MAIN.fabricInterface)

        val calls = functions.mapNotNull { fn ->
            try {
                FunctionCallResolver.resolve(fn, AnnotationType.MainEntrypoint) { msg, node ->
                    logger.error(msg, node)
                    null
                }?.also { call ->
                    val className = call.substringBefore("(").substringBeforeLast(".")
                    if (className.isNotEmpty() && !className.contains("(")) {
                        imports.add(className)
                    }
                }
            } catch (ex: Exception) {
                logger.error(
                    "Failed to resolve function call: ${fn.simpleName.asString()} - ${ex.message}",
                    fn
                )
                null
            }
        }

        writeMainEntrypoint(
            file,
            packageName,
            imports,
            calls
        )
    }
}