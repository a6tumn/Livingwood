package net.autumn.livingwood.util

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate
import kotlin.sequences.forEach

fun <T : KSAnnotated> Resolver.collect(
    annotation: String,
    type: Class<T>,
    deferred: MutableList<KSAnnotated>? = null
): List<T> {
    val result = mutableListOf<T>()

    getSymbolsWithAnnotation(annotation)
        .filterIsInstance(type)
        .forEach { symbol ->
            if (!symbol.validate()) deferred?.add(symbol) else result += symbol
        }

    return result
}