package hr.onit.agency.agent

import hr.onit.agency.configuration.Configuration
import hr.onit.agency.logging.LoggingWrapper
import hr.onit.agency.routing.AgentInstructions
import hr.onit.agency.routing.Route
import hr.onit.agency.service_calls.ServiceCall
import hr.onit.agency.statistic.DataPoint
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class Agent(instructions: AgentInstructions) : Runnable {

    enum class Hook { PRE_RUN, POST_RUN, PRE_SERVICE_CALL, POST_SERVICE_CALL }

    val session: Session
    val route: Route

    // Hooks
    private val agentHooks = mutableMapOf<Hook, MutableMap<String, (Agent) -> Unit>>()
    private val agentServiceCallHooks = mutableMapOf<Hook, MutableMap<String, (Agent, ServiceCall) -> Unit>>()

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
        handleHooks(Hook.PRE_RUN)
        if (!route.isDone()) callService(route.next())
        else finished = true
        handleHooks(Hook.POST_RUN)
    }

    fun registerHook(type: Hook, hookId: String? = null, hook: (Agent) -> Unit) = agentHooks.getOrPut(type) { mutableMapOf() }.put(hookId ?: UUID.randomUUID().toString(), hook)

    fun registerHook(type: Hook, hookId: String? = null, hook: (Agent, ServiceCall) -> Unit) =
        agentServiceCallHooks.getOrPut(type) { mutableMapOf() }.put(hookId ?: UUID.randomUUID().toString(), hook)

    fun unregisterHook(type: Hook, hookId: String) {
        when (type) {
            Hook.PRE_RUN, Hook.POST_RUN -> agentHooks.getOrDefault(type, mutableMapOf()).remove(hookId)
            Hook.PRE_SERVICE_CALL, Hook.POST_SERVICE_CALL -> agentServiceCallHooks.getOrDefault(type, mutableMapOf()).remove(hookId)
        }
    }

    private fun callService(serviceCall: ServiceCall) {
        LoggingWrapper.debug("Agent", "Calling " + serviceCall.description())
        val start = LocalDateTime.now()
        try {
            handleHooks(Hook.PRE_SERVICE_CALL, serviceCall)
            serviceCall.execute(this)
            successes++
        } catch (e: Exception) {
            e.printStackTrace()
            errors++
        } finally {
            handleHooks(Hook.POST_SERVICE_CALL, serviceCall)
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

    private fun handleHooks(type: Hook, serviceCall: ServiceCall? = null) {
        when (type) {
            Hook.PRE_RUN, Hook.POST_RUN -> handleHooks(agentHooks.getOrDefault(type, emptyMap()))
            Hook.PRE_SERVICE_CALL, Hook.POST_SERVICE_CALL -> handleHooks(agentServiceCallHooks.getOrDefault(type, emptyMap()), serviceCall)
        }
    }

    private fun handleHooks(hooks: Map<String, (Agent) -> Unit>) {
        hooks.values.forEach { it.invoke(this) }
    }

    private fun handleHooks(hooks: Map<String, (Agent, ServiceCall) -> Unit>, serviceCall: ServiceCall?) {
        serviceCall?.let { call -> hooks.values.forEach { it.invoke(this, call) } }
    }
}
