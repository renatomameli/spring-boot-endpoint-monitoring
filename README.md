# Endpoint Metrics Service

This project provides a service that fetches all endpoints and their corresponding methods of a Spring Boot application and retrieves metrics for each endpoint using Spring Actuator. The metrics are then compiled into a list and returned, giving an overview of all endpoints and their metrics.

## Table of Contents
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Including in Your Project](#including-in-your-project)
- [Building the Project](#building-the-project)
- [Contributing](#contributing)
- [License](#license)

## Getting Started

### Prerequisites
- Java 11 or higher
- Gradle 6.0 or higher
- Spring Boot 2.5 or higher

### Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/<your-username>/endpoint-metrics-service.git
    cd endpoint-metrics-service
    ```

2. Build the project:

    ```sh
    ./gradlew build
    ```

## Usage

1. Ensure your Spring Boot application is running and the Actuator endpoints are enabled.

2. Inject the `EndpointMetricsService` into your controller or service where you need to fetch the endpoint metrics:

    ```kotlin
    @RestController
    class YourController(private val endpointMetricsService: EndpointMetricsService) {

        @GetMapping("/metrics")
        fun getMetrics(): List<EndpointMetrics> {
            return endpointMetricsService.getEndpointMetrics()
        }
    }
    ```

3. Access the metrics via the defined endpoint, e.g., `http://localhost:8080/metrics`.

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
        implementation 'com.example:endpoint-metrics-service:1.0.0'
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
        url = uri("https://maven.pkg.github.com/your-username/endpoint-metrics-service")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.token") ?: System.getenv("TOKEN")
        }
    }
    mavenCentral()
}

dependencies {
    implementation 'com.example:endpoint-metrics-service:1.0.0'
}
