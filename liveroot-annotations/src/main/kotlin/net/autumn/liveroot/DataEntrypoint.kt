package net.autumn.liveroot

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Suppress("unused")
annotation class DataEntrypoint(
    val type: DataEntrypointType,
    val lookup: KClass<*> = Any::class,
    val key: String = "__PROVIDER__"
)
