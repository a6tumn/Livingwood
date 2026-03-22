package net.autumn.livingwood.resolver

import com.google.devtools.ksp.symbol.KSDeclaration

object ImportResolver {
    fun resolve(declaration: KSDeclaration?): String? = declaration?.qualifiedName?.asString()
}