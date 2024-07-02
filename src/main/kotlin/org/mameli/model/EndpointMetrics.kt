package org.mameli.model

data class EndpointMetrics(
    val method: String,
    val endpoint: String,
    val metrics: List<Map<*, *>?>
)
