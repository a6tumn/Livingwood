package net.autumn.liveroot

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Suppress("unused")
annotation class Growth(
    val property: String
)
