package hr.onit.agency.routing

import hr.onit.agency.logging.LoggingWrapper
import okhttp3.internal.immutableListOf
import org.reflections.Reflections
import java.io.File
import java.nio.file.Files
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


class Schedule private constructor(private val assignments: List<AgentInstructions>) {

    private var next = 0

    companion object {
        fun init() = Schedule(immutableListOf(AgentInstructions(RandomRoute::class, emptyMap())))

        fun init(file: String): Schedule {
            @Suppress("UNCHECKED_CAST")
            val routesByName = Reflections("hr.onit").getTypesAnnotatedWith(hr.onit.agency.routing.annotation.Route::class.java)
                    .associateBy({ it.getAnnotation(hr.onit.agency.routing.annotation.Route::class.java).name }, { it::class as KClass<out Route> })

            val scheduleConfiguration = Files.readAllLines(File(file).toPath()).iterator()
            val instructions = mutableListOf<AgentInstructions>()

            var seedParameters = mutableMapOf<String, String>()
            var route: KClass<out Route> = RandomRoute::class
            while (scheduleConfiguration.hasNext()) {
                val line = scheduleConfiguration.next()
                LoggingWrapper.trace("Schedule", "Evaluating schedule line '"+line+"'")
                if (line.startsWith(' ') || line.startsWith('\t')) {
                    seedParameters.put(
                        line.substring(0, line.indexOf('=')).trim(),
                        line.substring(line.indexOf('=') + 1, line.length)
                    )
                } else {
                    instructions.add(AgentInstructions(route, seedParameters))
                    seedParameters= mutableMapOf()
                    route = routesByName.get(line.trim())?:RandomRoute::class
                }

            }
            LoggingWrapper.trace("Schedule", "Final instruction set is "+instructions)
            return Schedule(instructions)
        }

    }

    fun nextInstructions() = assignments[next % assignments.size]
}