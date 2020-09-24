package com.onit.routing

import com.onit.configuration.Configuration
import com.onit.service_calls.Done
import com.onit.service_calls.ServiceCall
import kotlin.reflect.full.primaryConstructor

class RandomRoute : Route {

    private var serviceCall: ServiceCall? = null

    override fun isDone(): Boolean {
        return when (serviceCall) {
            Done() -> true
            else -> false
        }
    }

    override fun next(): ServiceCall {
        if (null == serviceCall) serviceCall = Configuration.getInitialRandomServiceCall().primaryConstructor?.call()
        else {
            val possibleNextCalls = RouteMap.nextCallsFor(serviceCall?.let { it::class } ?: Done::class)
            val nextCall =
                (possibleNextCalls.get(Configuration.random.nextInt(possibleNextCalls.size)).primaryConstructor?.call()
                    ?: Done())
            serviceCall = nextCall
        }
        return serviceCall ?: Done()
    }
}