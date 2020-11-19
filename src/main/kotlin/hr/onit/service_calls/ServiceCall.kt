package hr.onit.service_calls

import hr.onit.agent.Agent
import hr.onit.agent.SessionKeys
import hr.onit.configuration.Configuration
import kotlin.reflect.KClass

abstract class ServiceCall {
    fun basePath():String {
        return Configuration.getBaseUrl()
    }
    abstract fun execute(agent: Agent)
    abstract fun requiredSessionValues(): List<SessionKeys>
    abstract fun nextPossibleCalls(): List<KClass<out ServiceCall>>
    open fun description() = this::class.simpleName!!
}