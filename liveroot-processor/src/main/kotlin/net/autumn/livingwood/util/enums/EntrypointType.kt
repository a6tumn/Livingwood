package net.autumn.livingwood.util.enums

enum class EntrypointType(
    val generatedClassName: String,
    val fabricInterface: String
) {
    MAIN(
        generatedClassName = GeneratedClassName.MAIN.simpleName,
        fabricInterface = FabricInterface.MAIN.qualifiedName
    ),
    CLIENT(
        generatedClassName = GeneratedClassName.CLIENT.simpleName,
        fabricInterface = FabricInterface.CLIENT.qualifiedName
    ),
    DATA(
        generatedClassName = GeneratedClassName.DATA.simpleName,
        fabricInterface = FabricInterface.DATA.qualifiedName
    );
}