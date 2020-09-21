package com.onit.routing

import com.onit.service_calls.Done
import com.onit.service_calls.ServiceCall
import kotlin.reflect.KClass

 class RouteBuilder internal constructor(val route: List<Any>) {

    companion object {
        fun route(serviceCall: KClass<out ServiceCall>) = RouteBuilder(listOf(serviceCall))
    }

     fun then(serviceCall: KClass<out ServiceCall>) : RouteBuilder {
         val newRoute = ArrayList(route)
         newRoute.add(serviceCall)
         return RouteBuilder(newRoute)
     }

     fun thenEither(serviceCall: KClass<out ServiceCall>) = OrRouteBuilder(route, listOf(serviceCall))

     fun thenDone() = RouteScript(then(Done::class).route)
}