# Endpoint Metrics Service

This project provides a org.mameli.model.service that fetches all endpoints and their corresponding methods of a Spring Boot application and retrieves metrics for each endpoint using Spring Actuator. The metrics are then compiled into a list and returned, giving an overview of all endpoints and their metrics.

## Table of Contents
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Including in Your Project](#including-in-your-project)
- [Building the Project](#building-the-project)
- [Contributing](#contributing)
- [License](#license)

## Getting Started

### Prerequisites
- Java 17
- Spring Boot 3.3

### Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/<your-username>/endpoint-metrics-org.mameli.model.service.git
    cd endpoint-metrics-org.mameli.model.service
    ```

2. Build the project:

    ```sh
    ./gradlew build
    ```

## Usage

1. Ensure your Spring Boot application is running and the Actuator endpoints are enabled. E.g. in application.yml:
```
management:
  endpoints:
    web:
      exposure:
        include: info, health, metrics, mappings
```

2. Inject the `EndpointMetricsService` into your controller or org.mameli.model.service where you need to fetch the endpoint metrics:

    ```kotlin
    @RestController
    class YourController(private val endpointMetricsService: EndpointMetricsService) {

        @GetMapping("/metrics")
        fun getMetrics(): List<EndpointMetrics> {
            return endpointMetricsService.getEndpointMetrics()
        }
    }
    ```

3. Scan the configuration of this package: `@ComponentScan(basePackages = ["com.your.packages", "org.mameli"])`

4. Access the metrics via the defined endpoint, e.g., `http://localhost:8080/metrics`.

## Including in Your Project

To include this project as a dependency in your project, follow these steps:

1. Add the GitHub Packages repository and dependency to your `build.gradle` file:

    ```groovy
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/<GitHub-Username>/<Repository-Name>")
            credentials {
                username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.token") ?: System.getenv("TOKEN")
            }
        }
        mavenCentral()
    }

    dependencies {
        implementation 'com.example:endpoint-metrics-org.mameli.model.service:1.0.0'
    }
    ```

2. Create a `gradle.properties` file with your GitHub credentials (or set them as environment variables):

    ```properties
    gpr.user=<GitHub-Username>
    gpr.token=<GitHub-Personal-Access-Token>
    ```

### Example

Here is an example of how to set up the `build.gradle` file:

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/your-username/endpoint-metrics-org.mameli.model.service")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.token") ?: System.getenv("TOKEN")
        }
    }
    mavenCentral()
}

dependencies {
    implementation 'com.example:endpoint-metrics-org.mameli.model.service:1.0.0'
}
