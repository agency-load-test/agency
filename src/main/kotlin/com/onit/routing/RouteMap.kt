package com.onit.routing

import com.onit.configuration.Configuration
import org.reflections.Reflections
import com.onit.service_calls.Done
import com.onit.service_calls.ServiceCall
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object RouteMap {

    val routeMap = LinkedHashMap<KClass<out ServiceCall>, MutableList<KClass<out ServiceCall>>>()

    fun init() {
        val serviceCalls = Reflections(Configuration.getServiceCallPackages()).getSubTypesOf(ServiceCall::class.java)
        serviceCalls
            .map { it.kotlin }
            .map { Pair(it, it.primaryConstructor?.call()) }
            .forEach {
                val kclass = it.first
                it.second?.reachableFrom()?.forEach {
                    val value = routeMap.getOrPut(it) { ArrayList() }
                    value.add(kclass)
                }
                if (it.second?.final() ?: false) {
                    val addDone = routeMap.getOrPut(it.first) {ArrayList()}
                    addDone.add(Done::class)
                }
            }
    }

    fun nextCallsFor(serviceCall: KClass<out ServiceCall>) = routeMap.getOrDefault(serviceCall, listOf(Done::class))
}