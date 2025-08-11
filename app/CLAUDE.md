# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Dr. Pick (결정박사 - 결정장애 박살내는 사람들)**는 맥락 기반 스마트 메뉴 추천 서비스입니다. Kotlin Spring Boot 3.5.4를 사용하여 Core API를 구현하고 있으며, Java 21 기반으로 개발됩니다.

### 서비스 목표
- 개인화된 설문을 통한 맞춤 메뉴 추천
- 날씨, 위치, 상황 등 맥락 정보 기반 필터링
- 결정장애를 해결하는 직관적이고 스마트한 추천 경험 제공

### 현재 개발 단계
Core API 구현 중 - MVP 우선 접근 방식으로 점진적 기능 추가

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

## 핵심 기능 및 설계

### 주요 기능 (계획)
- **맞춤 설문**: 다이어트 타입, 어제 섭취 음식, 식사 상황, 옷차림 등
- **맥락 기반 추천**: 날씨 연동, 상황별 필터링, 위치 기반 추천
- **스마트 필터링**: 개인 선호도와 실시간 맥락 정보 결합

### 핵심 Entity 구조
- **User**: 사용자 정보 및 선호도 관리
- **Menu**: 메뉴 정보 및 속성 데이터
- **Restaurant**: 음식점 정보 및 위치 데이터
- **SurveyResponse**: 설문 응답 및 추천 로직 기반 데이터

### 확장 계획 (검토 중)
- **MSA 구조**: Core API(Kotlin) → Kakao Bot(Go) → Weather Service(Go)
- **외부 연동**: 카카오 API, 날씨 API, 지도 API 통합
- **플랫폼 확장**: 카카오톡 봇, 웹 서비스 다중 채널 지원

## 개발 원칙
- **MVP 우선**: 핵심 기능 먼저 구현 후 점진적 확장
- **오버엔지니어링 방지**: 필요에 따른 기술 도입
- **기술 스택 유연성**: 요구사항 변화에 대응 가능한 구조 유지

## Project Status
Core API 구현을 위한 Spring Boot 프로젝트 초기 설정 완료. 현재 비즈니스 로직, 컨트롤러, 서비스, 엔티티 구현 준비 단계입니다.