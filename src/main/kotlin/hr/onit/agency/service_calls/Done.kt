package hr.onit.agency.service_calls

import hr.onit.agency.agent.Agent
import hr.onit.agency.logging.LoggingWrapper
import kotlin.reflect.KClass

class Done : ServiceCall() {

    companion object {
        val description = "Done"
    }

    override fun doExecute(agent: Agent) = LoggingWrapper.debug("Agent Info", "Agent is done.")

    override fun requiredSessionValues(): List<String> = ArrayList()

    override fun nextPossibleCalls(): List<KClass<out ServiceCall>> = ArrayList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun description() = description
}