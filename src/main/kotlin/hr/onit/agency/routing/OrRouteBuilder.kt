package hr.onit.agency.routing

import hr.onit.agency.service_calls.ServiceCall
import kotlin.reflect.KClass


class OrRouteBuilder internal constructor( val route: List<Any>, val orRoute:List<KClass<out ServiceCall>>) {

    fun then(serviceCall: KClass<out ServiceCall>): RouteBuilder {
        val newRoute = ArrayList(route)
        newRoute.add(orRoute)
        newRoute.add(serviceCall)
        return RouteBuilder(newRoute)
    }

    fun or(serviceCall: KClass<out ServiceCall>) : OrRouteBuilder {
        val newOrRoute = ArrayList(orRoute)
        newOrRoute.add(serviceCall)
        return OrRouteBuilder(route, newOrRoute)
    }
}