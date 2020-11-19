package hr.onit.agency.service_calls

import hr.onit.agency.agent.Agent
import hr.onit.agency.configuration.Configuration
import kotlin.reflect.KClass

abstract class ServiceCall {
    fun basePath():String {
        return Configuration.getBaseUrl()
    }
    abstract fun execute(agent: Agent)
    abstract fun requiredSessionValues(): List<String>
    abstract fun nextPossibleCalls(): List<KClass<out ServiceCall>>
    open fun description() = this::class.simpleName!!
}