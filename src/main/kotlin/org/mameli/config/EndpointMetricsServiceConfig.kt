package org.mameli.config

import org.mameli.service.EndpointMetricsService
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class EndpointMetricsServiceConfig {

    @Bean
    open fun endpointMetricsService(webServerAppCtx: ServletWebServerApplicationContext,
                                    applicationContext: ApplicationContext
    ): EndpointMetricsService {
        println("Create EndpointMetricsService")
        return EndpointMetricsService(webServerAppCtx, applicationContext)
    }
}
