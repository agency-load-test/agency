package hr.onit.agency.routing

import kotlin.reflect.KClass

data class AgentInstructions(val route: KClass<out Route>, val seedParameters:Map<String, String> )