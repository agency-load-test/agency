package hr.onit.agency.routing

import hr.onit.agency.logging.LoggingWrapper
import hr.onit.agency.service_calls.ServiceCall
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

interface Route {
    fun isDone(): Boolean
    fun next(): ServiceCall

    companion object {
        fun instantiate(route: KClass<out Route>): Route {
            val instance = route.primaryConstructor?.call()
            if (null == instance)
                LoggingWrapper.warn("Routing", "Could not instantiate scheduled route " + route.simpleName + ", assigning RandomRoute instead");
            return instance ?: RandomRoute()
        }
    }
}