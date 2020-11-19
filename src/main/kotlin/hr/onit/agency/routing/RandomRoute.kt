package hr.onit.agency.routing

import hr.onit.agency.configuration.Configuration
import hr.onit.agency.routing.annotation.Route
import hr.onit.agency.service_calls.Done
import hr.onit.agency.service_calls.ServiceCall
import kotlin.reflect.full.primaryConstructor

@Route(name="Random")
class RandomRoute : hr.onit.agency.routing.Route {

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