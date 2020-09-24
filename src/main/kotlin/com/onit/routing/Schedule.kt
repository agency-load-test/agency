package com.onit.routing

import java.io.File
import java.nio.file.Files
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


class Schedule private constructor(private val assignments: Array<KClass<out Route>>) {

    private var next = 0

    companion object {
        fun init() = Schedule(arrayOf(RandomRoute::class))

        fun init(file: String) = Schedule(Files.readAllLines(File(file).toPath())
            .map { Class.forName(it.trim()) }
            .map{ it.kotlin}
            .map {
                @Suppress("UNCHECKED_CAST")
                it as KClass<out Route>
            }
            .toTypedArray())
    }

    fun nextRoute() = assignments[next % assignments.size].primaryConstructor?.call()
}