package service

import model.EndpointMetrics
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class EndpointMetricsService(
    webServerAppCtx: ServletWebServerApplicationContext,
    @Value("\${spring.application.name}") private val appName: String
) {

    private val restTemplate = RestTemplate()
    private val port = webServerAppCtx.webServer.port

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun getEndpointMetrics(): List<EndpointMetrics> {
        val actuatorMappings = restTemplate.getForObject("http://localhost:$port/actuator/mappings", Map::class.java) ?: return emptyList()
        return getEndpointMetricsListByActuatorMappings(actuatorMappings)
    }

    private fun getEndpointMetricsListByActuatorMappings(actuatorMappings: Map<*, *>): List<EndpointMetrics> {
        val endpointMetricsList = mutableListOf<EndpointMetrics>()
        val dispatcherServlet = getDispatcherServlet(actuatorMappings)

        dispatcherServlet?.forEach { mapping ->
            val details = mapping["details"] as Map<*, *>?
            val requestMappingConditions = details?.get("requestMappingConditions") as Map<*, *>?
            val methods = requestMappingConditions?.get("methods") as List<String>?
            val patterns = requestMappingConditions?.get("patterns") as List<String>?

            methods?.forEach { method ->
                patterns?.filter { endpoint -> !endpoint.startsWith("/actuator") } // TODO Make configurable
                    ?.forEach { endpoint ->
                        try {
                            val url = "http://localhost:$port/actuator/metrics/http.server.requests?tag=uri:$endpoint,method:$method"
                            val metrics = restTemplate.getForObject(url, Map::class.java)
                            endpointMetricsList.add(EndpointMetrics(method, endpoint, metrics?.get("measurements") as List<Map<*, *>>))
                        } catch (e: HttpClientErrorException.NotFound) {
                            log.debug("Endpoint: {} with method: {} not found", endpoint, method, e)
                        } catch (e: HttpClientErrorException) {
                            log.debug("An error occurred for endpoint: {} and the method: {}", endpoint, method, e)
                        }
                }
            }
        }
        return endpointMetricsList
    }

    private fun getDispatcherServlet(actuatorMappings: Map<*, *>): List<Map<*, *>>? {
        val contexts = actuatorMappings["contexts"] as Map<*, *>?
        val applicationContext = contexts?.get(appName) as Map<*, *>?
        val mappingsData = applicationContext?.get("mappings") as Map<*, *>?
        val dispatcherServlets = mappingsData?.get("dispatcherServlets") as Map<*, *>?
        return dispatcherServlets?.get("dispatcherServlet") as List<Map<*, *>>?
    }

}
