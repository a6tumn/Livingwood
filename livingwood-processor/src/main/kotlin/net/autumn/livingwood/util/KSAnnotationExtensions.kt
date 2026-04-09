package net.autumn.livingwood.util

import com.google.devtools.ksp.symbol.KSAnnotation

fun KSAnnotation.getArgument(
    name: String
): Any? =
    arguments.firstOrNull { it.name?.asString() == name }
        ?.value
        ?.toString()
        ?.substringAfterLast(".")