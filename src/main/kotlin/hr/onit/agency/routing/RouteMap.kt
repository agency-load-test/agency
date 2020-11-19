package hr.onit.agency.routing

import hr.onit.agency.configuration.Configuration
import hr.onit.agency.service_calls.Done
import hr.onit.agency.service_calls.ServiceCall
import okhttp3.internal.immutableListOf
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object RouteMap {

    val routeMap = LinkedHashMap<KClass<out ServiceCall>, List<KClass<out ServiceCall>>>()

    fun init() {
        val serviceCalls = Reflections(Configuration.getServiceCallPackages()).getSubTypesOf(ServiceCall::class.java)
        serviceCalls
            .map { it.kotlin }
            .forEach {
                val instance = it.primaryConstructor?.call()
                routeMap.put(it, instance?.nextPossibleCalls() ?: immutableListOf(Done::class))
            }
//        ApiClient.builder.authenticator(UserNamePasswordAuthenticator())
    }

    fun nextCallsFor(serviceCall: KClass<out ServiceCall>) = routeMap.getOrDefault(serviceCall, listOf(Done::class))
}