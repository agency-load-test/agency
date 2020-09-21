package com.onit.routing

import com.onit.configuration.Configuration
import com.onit.service_calls.Done
import com.onit.service_calls.ServiceCall
import kotlin.reflect.full.primaryConstructor

class RandomRoute : Route {

    private var next = Configuration.getInitialRandomServiceCall().primaryConstructor?.call()

    override fun isDone(): Boolean {
        return when (next) {
            Done() -> true
            null -> true
            else -> false
        }
    }

    override fun next(): ServiceCall {
        val current = next
        val possibleNextCalls = RouteMap.nextCallsFor(current?.let { it::class } ?: Done::class)
        val nextCall =
            (possibleNextCalls.get(Configuration.random.nextInt(possibleNextCalls.size)).primaryConstructor?.call()
                ?: Done())
        next = nextCall
        return current ?: Done()
    }
}