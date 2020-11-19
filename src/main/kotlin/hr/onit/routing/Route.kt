package hr.onit.routing

import hr.onit.service_calls.ServiceCall
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

interface Route {
    fun isDone(): Boolean
    fun next(): ServiceCall

    companion object {
        fun instantiate(route: KClass<out Route>): Route {
            val instance = route.primaryConstructor?.call()
            if (null == instance)
                println("Could not instantiate scheduled route " + route.simpleName + ", assigning RandomRoute instead");
            return instance ?: RandomRoute()
        }
    }
}