# 기술 스택 및 환경 설정

이 문서는 PlanIt 프로젝트의 백엔드 개발에 사용될 기술 스택과 환경 설정을 정의합니다.

## 1. 언어 및 프레임워크

*   **Language**: Kotlin 2.0.0
*   **Framework**: Spring Boot 3.3.1
*   **JDK**: Java 21 (LTS)

## 2. 빌드 도구

*   **Build Tool**: Gradle

## 3. 데이터베이스

*   **Development DB**: H2 (In-Memory)
*   **Production DB**: PostgreSQL

## 4. 기타

*   **형상 관리**: Git
*   **배포 환경**: Docker, GitHub Actions (기획서 기반)

## 5. 테스트 스택

*   **Test Framework**: `JUnit 5`
*   **Assertion Library**: `AssertJ`
*   **Mocking Library**: `MockK`
*   **Integration Test Support**: `Spring Test`
*   **DB Integration Test**: `Testcontainers`

## 6. 프로젝트 구성 (Project Configuration)

*   **Project Name (Artifact ID)**: `planit-backend`
*   **Package Name (Group ID)**: `com.planit` 