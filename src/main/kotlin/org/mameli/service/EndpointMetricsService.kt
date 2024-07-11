package org.mameli.service

import org.mameli.extension.getByKeyAsList
import org.mameli.extension.getByKeyAsMap
import org.mameli.extension.getByKeyAsMapList
import org.mameli.extension.getByKeyAsStringList
import org.mameli.model.EndpointMetrics
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class EndpointMetricsService(
    webServerAppCtx: ServletWebServerApplicationContext,
    private val applicationContext: ApplicationContext
) {

    private val restTemplate = RestTemplate()
    private val port = webServerAppCtx.webServer.port
    private val actuatorMappingsEndpoint = "http://localhost:$port/actuator/mappings"

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun getEndpointMetrics(): List<EndpointMetrics> {
        try {
            val actuatorMappings = restTemplate.getForObject(this.actuatorMappingsEndpoint, Map::class.java)
            if (actuatorMappings == null) {
                log.info("No actuator mappings found.")
                return emptyList()
            }
            return this.parseEndpointMetrics(actuatorMappings)
        } catch (e: Exception) {
            log.info("Failed to fetch actuator mappings.", e)
        }
        return emptyList()
    }

    private fun parseEndpointMetrics(actuatorMappings: Map<*, *>): List<EndpointMetrics> {
        val endpointMetricsList = mutableListOf<EndpointMetrics>()
        val dispatcherServlet = getDispatcherServletByActuatorMappings(actuatorMappings)

        dispatcherServlet?.forEach { mapping ->
            val requestMappingConditions = this.getRequestMappingConditions(mapping)
            val methods = requestMappingConditions?.getByKeyAsStringList("methods")
            val patterns = requestMappingConditions?.getByKeyAsStringList("patterns")

            methods?.forEach { method ->
                patterns?.filter { endpoint -> !endpoint.startsWith("/actuator") } // TODO Make configurable
                    ?.forEach { endpoint -> fetchAndAddEndpointMetrics(endpointMetricsList, method, endpoint) }
            }
        }

        return endpointMetricsList
    }

    private fun fetchAndAddEndpointMetrics(
        endpointMetricsList: MutableList<EndpointMetrics>,
        method: String,
        endpoint: String
    ) {
        try {
            val url = "http://localhost:$port/actuator/metrics/http.server.requests?tag=uri:$endpoint,method:$method"
            val metrics = restTemplate.getForObject(url, Map::class.java)
            val measurements = metrics?.getByKeyAsList("measurements")
            if (measurements != null) {
                endpointMetricsList.add(EndpointMetrics(method, endpoint, measurements))
            }
        } catch (e: HttpClientErrorException.NotFound) {
            log.debug("Endpoint: {} with method: {} not found", endpoint, method, e)
        } catch (e: HttpClientErrorException) {
            log.debug("An error occurred for endpoint: {} and the method: {}", endpoint, method, e)
        }
    }

    private fun getDispatcherServletByActuatorMappings(actuatorMappings: Map<*, *>): List<Map<*, *>>? {
        return actuatorMappings.getByKeyAsMap("contexts")
            ?.getByKeyAsMap(applicationContext.id)
            ?.getByKeyAsMap("mappings")
            ?.getByKeyAsMap("dispatcherServlets")
            ?.getByKeyAsMapList("dispatcherServlet")
    }

    private fun getRequestMappingConditions(actuatorMapping: Map<*, *>): Map<*, *>? {
        return actuatorMapping.getByKeyAsMap("details")?.getByKeyAsMap("requestMappingConditions")
    }

}
