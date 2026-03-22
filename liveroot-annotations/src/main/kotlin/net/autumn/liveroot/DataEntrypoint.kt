package net.autumn.liveroot

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Suppress("unused")
annotation class DataEntrypoint(
    val type: DataEntrypointType,
    val key: String = "__PROVIDER__"
)
