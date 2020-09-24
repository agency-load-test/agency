package com.onit.routing

import com.onit.configuration.Configuration
import com.onit.service_calls.Done
import com.onit.service_calls.ServiceCall
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
    }

    fun nextCallsFor(serviceCall: KClass<out ServiceCall>) = routeMap.getOrDefault(serviceCall, listOf(Done::class))
}