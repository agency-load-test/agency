package com.onit.routing

import java.io.File
import java.nio.file.Files
import kotlin.reflect.full.primaryConstructor


class Schedule private constructor(private val assignments: Array<Route>) {

    private var next = 0

    companion object {
        fun init() = Schedule(arrayOf(RandomRoute()))

        fun init(file: String) = Schedule(Files.readAllLines(File(file).toPath())
            .map { Class.forName(it.trim()) }
            .map{ it.kotlin}
            .map { it.primaryConstructor?.call() }
            .map { it as Route }
            .toTypedArray())
    }

    fun nextRoute() = assignments[next % assignments.size]
}