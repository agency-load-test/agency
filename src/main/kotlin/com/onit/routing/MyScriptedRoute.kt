package com.onit.routing

import com.onit.service_calls.PetStoreDetails
import com.onit.service_calls.PetStoreFindByStatusCall

class MyScriptedRoute() : ScriptedRoute() {

    override fun script() = RouteBuilder.route(PetStoreFindByStatusCall::class)
        .then(PetStoreFindByStatusCall::class)
        .then(PetStoreDetails::class)
        .then(PetStoreFindByStatusCall::class)
        .thenEither(PetStoreDetails::class).or(PetStoreFindByStatusCall::class)
        .then(PetStoreDetails::class)
        .thenDone()
}