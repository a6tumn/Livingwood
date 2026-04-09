package net.autumn.livingwood.aggregator

class ImportAggregator {
    private val imports = linkedSetOf<String>()

    fun add(
        qualifiedName: String?
    ) {
        if (null != qualifiedName) {
            imports += qualifiedName
        }
    }

    fun writeTo(
        writer: Appendable
    ) {
        imports.sorted().forEach {
            writer.appendLine("import $it")
        }
    }
}