package net.autumn.livingwood.resolver

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.Modifier
import net.autumn.livingwood.util.enums.AnnotationType

object FunctionCallResolver {

    fun resolve(
        function: KSFunctionDeclaration,
        annotationType: AnnotationType,
        error: (String, KSNode) -> Nothing?
    ): String? {
        val parent = function.parentDeclaration
        val functionName = function.simpleName.asString()
        val shortAnnotation = annotationType.simpleName

        return when (parent) {
            is KSClassDeclaration -> resolveInClass(parent, function, functionName, shortAnnotation, error)

            null -> {
                val file = function.containingFile ?: return error("Top-level function must have a containing file", function)
                val pkg = function.packageName.asString()
                val fileName = file.fileName.removeSuffix(".kt")
                "$pkg.${fileName}Kt.$functionName()"
            }

            else -> error("@$shortAnnotation only allowed on top-level or class/object functions", function)
        }
    }

    private fun resolveInClass(
        parent: KSClassDeclaration,
        function: KSFunctionDeclaration,
        functionName: String,
        shortAnnotation: String,
        error: (String, KSNode) -> Nothing?
    ): String? {
        val qualifiedName = parent.qualifiedName?.asString() ?: return error("Class must have a qualified name", function)

        return when (parent.classKind) {
            ClassKind.OBJECT -> {
                if (parent.isCompanionObject) {
                    val enclosing = parent.parentDeclaration as KSClassDeclaration
                    val enclosingName = enclosing.qualifiedName?.asString() ?: return error("Enclosing class must have a qualified name", function)
                    "$enclosingName.$functionName()"
                } else {
                    "$qualifiedName.$functionName()"
                }
            }

            ClassKind.CLASS -> {
                if (function.isStatic())
                    "$qualifiedName.$functionName()"
                else
                    "$qualifiedName().$functionName()"
            }

            else -> error("@$shortAnnotation not supported here", function)
        }
    }

    private fun KSFunctionDeclaration.isStatic(): Boolean = modifiers.contains(Modifier.JAVA_STATIC)
}