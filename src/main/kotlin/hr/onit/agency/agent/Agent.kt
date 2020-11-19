package hr.onit.agency.agent

import hr.onit.agency.configuration.Configuration
import hr.onit.agency.logging.LoggingWrapper
import hr.onit.agency.routing.AgentInstructions
import hr.onit.agency.routing.Route
import hr.onit.agency.service_calls.ServiceCall
import hr.onit.agency.statistic.DataPoint
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Agent(instructions: AgentInstructions) : Runnable {
    val session: Session
    val route: Route

    init {
        session = Session()
        LoggingWrapper.trace("Agent", "Using route " + instructions.route.simpleName)
        route = Route.instantiate(instructions.route)
        instructions.seedParameters.forEach {
            LoggingWrapper.trace("Agent", "Seeding parameter " + it.key + " with value '"+it.value+"'")
            session.put(it.key, it.value)
        }
    }

    var finished = false
    var successes = 0
    var errors = 0

    val requestDurationStatistic = ArrayList<DataPoint>()

    override fun run() {
        if (!route.isDone()) callService(route.next())
        else finished = true
    }

    private fun callService(serviceCall: ServiceCall) {
        LoggingWrapper.debug("Agent", "Calling " + serviceCall.description())
        val start = LocalDateTime.now()
        try {
            serviceCall.execute(this)
            successes++
        } catch (e: Exception) {
            e.printStackTrace()
            errors++
        }
        requestDurationStatistic.add(
            DataPoint(
                start,
                serviceCall.description(),
                start.until(LocalDateTime.now(), ChronoUnit.MILLIS).toDouble()
            )
        )

        if (!route.isDone()) {
            Thread.sleep(Configuration.getCallDelayInSec() * 1000L)
            callService(route.next())
        } else {
            finished = true
        }
    }
}