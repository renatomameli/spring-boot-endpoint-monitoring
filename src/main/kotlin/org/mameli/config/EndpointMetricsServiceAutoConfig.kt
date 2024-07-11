package org.mameli.config

import org.mameli.service.EndpointMetricsService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean

@AutoConfiguration
open class EndpointMetricsServiceAutoConfig {

    @Bean
    open fun endpointMetricsService(
        webServerAppCtx: ServletWebServerApplicationContext,
        applicationContext: ApplicationContext
    ): EndpointMetricsService = EndpointMetricsService(webServerAppCtx, applicationContext)
}
