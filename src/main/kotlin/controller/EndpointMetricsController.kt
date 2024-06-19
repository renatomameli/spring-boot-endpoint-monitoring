import model.EndpointMetrics
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import service.EndpointMetricsService

@RestController
class EndpointMetricsController(private val endpointMetricsService: EndpointMetricsService) {

    @GetMapping("/endpoint-metrics")
    fun getEndpointsMetrics(): List<EndpointMetrics> {
        return endpointMetricsService.getEndpointMetrics()
    }
}