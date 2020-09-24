package com.onit.service_calls

import com.onit.agent.Agent
import com.onit.agent.SessionKeys
import com.onit.configuration.Configuration
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