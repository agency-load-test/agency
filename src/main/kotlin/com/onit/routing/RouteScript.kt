package com.onit.routing

class RouteScript(private val route: List<Any>) {
    fun length() = route.size
    fun get(index: Int) = route.get(index)
}