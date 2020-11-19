package hr.onit.routing

import hr.onit.routing.annotation.Route
import hr.onit.service_calls.PetStoreDetails
import hr.onit.service_calls.PetStoreFindByStatusCall

@Route(name="MyScripted")
class MyScriptedRoute : ScriptedRoute() {

    override fun script() = RouteBuilder.route(PetStoreFindByStatusCall::class)
        .then(PetStoreFindByStatusCall::class)
        .then(PetStoreDetails::class)
        .then(PetStoreFindByStatusCall::class)
        .thenEither(PetStoreDetails::class).or(PetStoreFindByStatusCall::class)
        .then(PetStoreDetails::class)
        .thenDone()
}