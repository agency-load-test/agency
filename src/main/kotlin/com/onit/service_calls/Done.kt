package com.onit.service_calls

import com.onit.agent.Agent
import com.onit.agent.SessionKeys
import kotlin.reflect.KClass

class Done : ServiceCall() {
    override fun execute(agent: Agent) = println("Agent is done.")

    override fun requiredSessionValues(): List<SessionKeys> = ArrayList()

    override fun nextPossibleCalls(): List<KClass<out ServiceCall>> = ArrayList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


}