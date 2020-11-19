package hr.onit.agent

import hr.onit.configuration.Configuration
import hr.onit.routing.AgentInstructions
import hr.onit.routing.Route
import hr.onit.service_calls.ServiceCall
import hr.onit.statistic.DataPoint
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Agent(instructions: AgentInstructions) : Runnable {
    val session: Session
    val route: Route

    init {
        session = Session()
        route = Route.instantiate(instructions.route)
        instructions.seedParameters.forEach { session.put(SessionKeys.valueOf(it.key), it.value) }
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
        println("Calling " + serviceCall.description())
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