# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Dr. Pick App is a Kotlin Spring Boot 3.5.4 application using Java 21. The project follows standard Spring Boot architecture patterns and is configured for web development with database persistence (PostgreSQL) and caching (Redis).

## Common Commands

### Build and Run
```bash
# Build the application
./gradlew build

# Run the application locally
./gradlew bootRun

# Create executable JAR
./gradlew bootJar

# Clean build artifacts
./gradlew clean
```

### Testing
```bash
# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "com.drpick.app.SpecificTestClass"

# Run tests with debug output
./gradlew test --info
```

## Architecture and Structure

### Technology Stack
- **Framework**: Spring Boot 3.5.4 with Spring Web, Spring Data JPA, Spring Data Redis
- **Language**: Kotlin 1.9.25 targeting Java 21
- **Build Tool**: Gradle 8.14.3
- **Database**: PostgreSQL (runtime)
- **Cache**: Redis
- **Testing**: JUnit 5 with Spring Boot Test

### Package Structure
- **Main Package**: `com.drpick.app`
- **Entry Point**: `AppApplication.kt` with `@SpringBootApplication`
- **Resources**: Configuration in `application.properties`

### Key Gradle Plugins
- `kotlin("plugin.spring")` - Spring support for Kotlin
- `kotlin("plugin.jpa")` - JPA support with `allOpen` configuration for entities
- Spring Boot plugin with dependency management

### Development Setup
- Spring Boot DevTools included for hot reloading
- Configuration processor for IDE autocomplete support
- JPA entities automatically configured with `allOpen` plugin for Entity, MappedSuperclass, and Embeddable annotations

## Project Status
This is a freshly initialized Spring Boot project with minimal implementation. The application structure is in place but lacks business logic, controllers, services, and entities. The project is ready for implementing REST APIs and database models.