package hr.onit.agency.agent

import hr.onit.agency.exception.MissingInputException
import hr.onit.agency.logging.LoggingWrapper

class Session {
    private val values = HashMap<String, Any>()

    fun get(key: String): Any {
        return if (values.contains(key)) values.getOrDefault(
            key,
            ""
        ) else throw MissingInputException("Input for key " + key + " expected in session, but not found. Check your routing.")
    }

    fun put(key: String, value: Any) {
        LoggingWrapper.trace("Session", "Putting value " + value + " to key " + key)
        values.put(key, value)
    }

}

