package net.autumn.livingwood.resolver

object NameResolver {
    fun resolve(
        base: String,
        className: String
    ): String {
        return base.ifBlank { className }
    }
}