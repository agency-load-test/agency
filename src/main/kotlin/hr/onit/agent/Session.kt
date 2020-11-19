package hr.onit.agent

import hr.onit.exception.MissingInputException
import org.openapitools.client.infrastructure.ApiClient

class Session {
    private val values = HashMap<SessionKeys, Any>()

    fun get(key: SessionKeys): Any {
        return if (values.contains(key)) values.getOrDefault(
            key,
            ""
        ) else throw MissingInputException("Input for key " + key + " expected in session, but not found. Check your routing.")
    }

    fun put(key: SessionKeys, value: Any) {
        values.put(key, value)
    }

    fun setAccessToken( accessToken : String) {
        ApiClient.accessToken = accessToken
    }
}

