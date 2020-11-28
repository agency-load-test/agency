package hr.onit.agency.service_calls

import hr.onit.agency.agent.Agent
import hr.onit.agency.configuration.Configuration
import kotlin.reflect.KClass

abstract class ServiceCall {

    val preExecute = mutableListOf<(ServiceCall, Agent) -> Unit>()
    val postExecute = mutableListOf<(ServiceCall, Agent) -> Unit>()

    fun basePath():String {
        return Configuration.getBaseUrl()
    }

    fun execute(agent: Agent) {
        handleHooks(preExecute, agent)
        doExecute(agent)
        handleHooks(postExecute, agent)
    }

    fun registerPreExecuteHook(hook:(ServiceCall, Agent) -> Unit) = preExecute.add(hook)
    fun unregisterPreExecuteHook(hook:(ServiceCall, Agent) -> Unit) = preExecute.remove(hook)
    fun registerPostExecuteHook(hook:(ServiceCall, Agent) -> Unit) = postExecute.add(hook)
    fun unregisterPostExecuteHook(hook:(ServiceCall, Agent) -> Unit) = postExecute.remove(hook)

    abstract fun doExecute(agent: Agent)
    abstract fun requiredSessionValues(): List<String>
    abstract fun nextPossibleCalls(): List<KClass<out ServiceCall>>
    open fun description() = this::class.simpleName!!

    private fun handleHooks(hooks: MutableList<(ServiceCall, Agent) -> Unit>, agent: Agent) {
        hooks.forEach { it.invoke(this, agent) }
    }
}