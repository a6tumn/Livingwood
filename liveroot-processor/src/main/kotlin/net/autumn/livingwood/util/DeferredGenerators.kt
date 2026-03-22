package net.autumn.livingwood.util

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import net.autumn.livingwood.generator.ClientEntrypointGenerator
import net.autumn.livingwood.generator.DataEntrypointGenerator
import net.autumn.livingwood.generator.LivingTreeGenerator
import net.autumn.livingwood.generator.MainEntrypointGenerator

fun generateMainEntrypoint(
    functions: List<KSFunctionDeclaration>,
    codeGenerator: CodeGenerator,
    logger: KSPLogger
) {
    MainEntrypointGenerator(codeGenerator, logger)
        .generate(functions)
}

fun generateClientEntrypoint (
    functions: List<KSFunctionDeclaration>,
    codeGenerator: CodeGenerator,
    logger: KSPLogger
) {
    ClientEntrypointGenerator(codeGenerator, logger)
        .generate(functions)
}

fun generateDataEntrypoint(
    classes: List<KSClassDeclaration>,
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
) {
    DataEntrypointGenerator(codeGenerator, logger)
        .generate(classes)
}

fun generateLivingTree(
    classes: List<KSClassDeclaration>,
    codeGenerator: CodeGenerator,
    logger: KSPLogger
) {
    LivingTreeGenerator(codeGenerator, logger)
        .generate(classes)
}