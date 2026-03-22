package net.autumn.livingwood.aggregator

class NameAggregator {
    private val usedNames = mutableSetOf<String>()

    fun add(
        suggestedName: String,
        className: String
    ): String {
        val finalName = if (suggestedName in usedNames) "${className}_$suggestedName" else suggestedName
        usedNames += finalName
        return finalName
    }

    fun clear() {
        usedNames.clear()
    }
}