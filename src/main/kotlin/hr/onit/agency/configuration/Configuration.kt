package hr.onit.agency.configuration

import com.natpryce.konfig.*
import hr.onit.agency.service_calls.ServiceCall
import okhttp3.internal.immutableListOf
import java.io.File
import java.nio.file.Path
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object Configuration {

    val builtInServiceCallsPackage = "hr.onit.agency.service_calls"
    val config = ConfigurationProperties.fromResource("agency.properties") overriding
            ConfigurationProperties.fromResource("defaults.properties")
    val random = Random(0)

    fun getBaseUrl(): String {
        return get(Key("rest.baseUrl", stringType))
    }

    fun getErrorThreshold(): Int {
        return getOrDefault(Key("result.error.threshold", intType), 0)
    }

    fun getCallDelayInSec(): Int {
        return getOrDefault(Key("call.delay.sec", intType), 1)
    }

    fun getSimulationStepDelayInSec(): Int {
        return getOrDefault(Key("simulation.step.delay.sec", intType), 1)
    }

    fun getOutputFile(): Path {
        return File(getOrDefault(Key("output.fileName", stringType), "/tmp/agency/plot.html")).toPath()
    }

    fun getServiceCallPackages(): List<String> {
        val packages = mutableListOf(builtInServiceCallsPackage)
        packages.addAll(get(Key("service.call.packages", stringType)).split(";"))
        return packages
    }

    fun getInitialRandomServiceCall(): KClass<out ServiceCall> {
        val initialServiceCallClassName = get(Key("initial.random.service.call", stringType))
        val configuredClass = Class.forName(initialServiceCallClassName).kotlin
        @Suppress("UNCHECKED_CAST")
        return if (configuredClass.isSubclassOf(ServiceCall::class)) configuredClass as KClass<out ServiceCall>
        else throw Misconfiguration("Configured initial service call class is not of type ServiceCall")
    }

    fun <T> get(configurationKey: Key<T>): T {
        return config.get(configurationKey)
    }

    fun <T> getOrDefault(configurationKey: Key<T>, defaultValue: T): T {
        return config.getOrElse(configurationKey, defaultValue)
    }
}