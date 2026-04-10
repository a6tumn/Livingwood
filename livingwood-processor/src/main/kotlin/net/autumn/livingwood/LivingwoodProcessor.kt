package net.autumn.livingwood

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import net.autumn.livingwood.util.collect
import net.autumn.livingwood.util.generateClientEntrypoint
import net.autumn.livingwood.util.generateDataEntrypoint
import net.autumn.livingwood.util.generateLivingTree
import net.autumn.livingwood.util.generateMainEntrypoint
import net.autumn.livingwood.util.enums.AnnotationType

class LivingwoodProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(
        resolver: Resolver
    ): List<KSAnnotated> {
        val deferred = mutableListOf<KSAnnotated>()

        val mainEntrypointAnnotations = resolver.collect(AnnotationType.MainEntrypoint.qualifiedName, KSFunctionDeclaration::class.java, deferred)
        val clientEntrypointAnnotations = resolver.collect(AnnotationType.ClientEntrypoint.qualifiedName, KSFunctionDeclaration::class.java, deferred)
        val dataEntrypointAnnotations = resolver.collect(AnnotationType.DataEntrypoint.qualifiedName, KSClassDeclaration::class.java, deferred)
        val growthAnnotations = resolver.collect(AnnotationType.Growth.qualifiedName, KSClassDeclaration::class.java, deferred)

        if(mainEntrypointAnnotations.isNotEmpty()) {
            generateMainEntrypoint(
                mainEntrypointAnnotations,
                codeGenerator,
                logger
            )
        }

        if(clientEntrypointAnnotations.isNotEmpty()) {
            generateClientEntrypoint(
                clientEntrypointAnnotations,
                codeGenerator,
                logger
            )
        }

        if(dataEntrypointAnnotations.isNotEmpty()) {
            generateDataEntrypoint(
                dataEntrypointAnnotations,
                codeGenerator,
                logger
            )
        }

        if(growthAnnotations.isNotEmpty()) {
            generateLivingTree(
                growthAnnotations,
                codeGenerator,
                logger
            )
        }

        return deferred
    }
}