package net.autumn.livingwood.util.enums

enum class AnnotationType(
    val qualifiedName: String,
) {
    MainEntrypoint("net.autumn.liveroot.MainEntrypoint"),
    ClientEntrypoint("net.autumn.liveroot.ClientEntrypoint"),
    DataEntrypoint("net.autumn.liveroot.DataEntrypoint"),
    Growth("net.autumn.liveroot.Growth"),
    Rot("net.autumn.liveroot.Rot");

    val simpleName: String
        get() = qualifiedName.substringAfterLast('.')
}