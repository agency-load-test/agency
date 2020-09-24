package com.onit.service_calls

import com.onit.agent.Agent
import com.onit.agent.SessionKeys
import com.onit.agency.client.api.PetApi
import kotlin.random.Random
import kotlin.reflect.KClass

class PetStoreFindByStatusCall() : ServiceCall() {
    val petApi = PetApi(basePath())
    val random = Random(9854)

    override fun execute(agent: Agent) {
        val petList =
            petApi.findPetsByStatus(arrayOf(agent.session.get(SessionKeys.PET_STATUS) as String))
        if (petList.size > 0) {
            val pet = petList.get(random.nextInt(petList.size))
            //println("Selected pet: $pet")
            pet.id?.let { agent.session.put(SessionKeys.PET_ID, it) }
        }
    }

    override fun requiredSessionValues(): List<SessionKeys> {
        return mutableListOf(SessionKeys.PET_STATUS)
    }

    override fun nextPossibleCalls(): List<KClass<out ServiceCall>> {
        return mutableListOf(PetStoreDetails::class, PetStoreFindByStatusCall::class)
    }

    override fun description() = "GET /pet/findByStatus?status={status}"
}