package hr.onit.routing

import hr.onit.configuration.Configuration
import hr.onit.service_calls.Done
import hr.onit.service_calls.ServiceCall
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class ScriptedRoute : Route {

    private var position = 0
    private val route = script()

    abstract fun script() : RouteScript

    override fun isDone() = position >= route.length()

    override fun next(): ServiceCall {
        val candidate = route.get(position++)
         val next = if (candidate is List<*>) getNextFromOr(candidate as List<KClass<out ServiceCall>>) else candidate as KClass<out ServiceCall>
        return instantiate(next)
    }

    private fun getNextFromOr(list: List<KClass<out ServiceCall>>) = list.get(Configuration.random.nextInt(list.size))

    private fun instantiate(serviceCallClass : KClass<out ServiceCall>)= serviceCallClass.primaryConstructor?.call() ?: Done()
}