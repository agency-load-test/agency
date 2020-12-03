package hr.onit.agency.service_calls

import hr.onit.agency.agent.Agent
import hr.onit.agency.configuration.Configuration
import java.util.*
import kotlin.reflect.KClass

abstract class ServiceCall {

    enum class Hook { PRE_EXECUTE, POST_EXECUTE }

    private val hooks = mutableMapOf<Hook, MutableMap<String, (ServiceCall, Agent) -> Unit>>()

    fun basePath():String {
        return Configuration.getBaseUrl()
    }

    fun execute(agent: Agent) {
        handleHooks(Hook.PRE_EXECUTE, agent)
        doExecute(agent)
        handleHooks(Hook.POST_EXECUTE, agent)
    }

    fun registerHook(type: Hook, hookId: String? = null,  hook: (ServiceCall, Agent) -> Unit) = hooks.getOrPut(type) { mutableMapOf() }.put(hookId?:UUID.randomUUID().toString(), hook)

    fun unregisterPreExecuteHook(type: Hook, hookId: String) = hooks.getOrDefault(type, mutableMapOf()).remove(hookId)

    abstract fun doExecute(agent: Agent)
    abstract fun requiredSessionValues(): List<String>
    abstract fun nextPossibleCalls(): List<KClass<out ServiceCall>>
    open fun description() = this::class.simpleName!!

    private fun handleHooks(type:Hook, agent: Agent) {
        hooks.getOrDefault(type, emptyMap()).values.forEach { it.invoke(this, agent) }
    }
}