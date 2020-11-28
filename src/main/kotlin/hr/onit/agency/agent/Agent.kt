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

    // Hooks
    val preRun = mutableListOf<(Agent) -> Unit>()
    val postRun = mutableListOf<(Agent) -> Unit>()
    val preServiceCall = mutableListOf<(Agent, ServiceCall) -> Unit>()
    val postServiceCall = mutableListOf<(Agent, ServiceCall) -> Unit>()

    var finished = false
    var successes = 0
    var errors = 0

    val requestDurationStatistic = ArrayList<DataPoint>()

    init {
        session = Session()
        LoggingWrapper.trace("Agent", "Using route " + instructions.route.simpleName)
        route = Route.instantiate(instructions.route)
        instructions.seedParameters.forEach {
            LoggingWrapper.trace("Agent", "Seeding parameter " + it.key + " with value '"+it.value+"'")
            session.put(it.key, it.value)
        }
    }

    override fun run() {
        handleHooks(preRun)
        if (!route.isDone()) callService(route.next())
        else finished = true
        handleHooks(postRun)
    }

    fun registerPreRunHook(hook:(Agent) -> Unit) = preRun.add(hook)

    fun unregisterPreRunHook(hook:(Agent) -> Unit) = preRun.remove(hook)

    fun registerPostRunHook(hook:(Agent) -> Unit) = postRun.add(hook)

    fun unregisterPostRunHook(hook:(Agent) -> Unit) = postRun.remove(hook)

    fun registerPreServiceCallHook(hook:(Agent, ServiceCall) -> Unit) = preServiceCall.add(hook)

    fun unregisterPreServiceCallHook(hook:(Agent, ServiceCall) -> Unit) = preServiceCall.remove(hook)

    fun registerPostServiceCallHook(hook:(Agent, ServiceCall) -> Unit) = postServiceCall.add(hook)

    fun unregisterPostServiceCallHook(hook:(Agent, ServiceCall) -> Unit) = postServiceCall.remove(hook)

    private fun callService(serviceCall: ServiceCall) {
        LoggingWrapper.debug("Agent", "Calling " + serviceCall.description())
        val start = LocalDateTime.now()
        try {
            handleHooks(preServiceCall, serviceCall)
            serviceCall.execute(this)
            successes++
        } catch (e: Exception) {
            e.printStackTrace()
            errors++
        } finally {
            handleHooks(postServiceCall, serviceCall)
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

    private fun handleHooks(hooks: MutableList<(Agent) -> Unit>) {
        hooks.forEach { it.invoke(this) }
    }

    private fun handleHooks(hooks: MutableList<(Agent, ServiceCall) -> Unit>, serviceCall: ServiceCall) {
        hooks.forEach { it.invoke(this, serviceCall) }
    }
}