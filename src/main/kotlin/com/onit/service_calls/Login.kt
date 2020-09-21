package com.onit.service_calls

import com.onit.agent.Agent
import com.onit.agent.SessionKeys
import kotlin.reflect.KClass

class Login : ServiceCall() {

    override fun execute(agent: Agent) {
        val loginApi = LoginApi("http://localhost:8080")
        loginApi.login("Login als ADVISOR_01", "aixigo")
    }

    override fun requiredSessionValues(): List<SessionKeys> = ArrayList()

    override fun reachableFrom(): List<KClass<out ServiceCall>> = ArrayList()
}