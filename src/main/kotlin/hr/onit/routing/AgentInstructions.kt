package hr.onit.routing

import kotlin.reflect.KClass

class AgentInstructions(val route: KClass<out Route>, val seedParameters:Map<String, String> )