# 🎯 Cursor 규칙 관리 시스템

객체지향 프로그래밍을 위한 체계적인 Cursor 규칙 관리 시스템입니다.

## 📁 디렉토리 구조

```
cursor-rules/
├── common/                 # 모든 프로그래밍 언어에 공통으로 적용되는 규칙
│   ├── general.cursorrules          # 일반적인 프로그래밍 원칙
│   ├── solid-principles.cursorrules # SOLID 원칙
│   ├── design-patterns.cursorrules  # 디자인 패턴
│   ├── testing.cursorrules          # 테스팅 규칙
│   ├── naming-conventions.cursorrules # 네이밍 컨벤션
│   └── code-quality.cursorrules     # 코드 품질 관리
│
├── languages/              # 언어별 특화 규칙
│   ├── java/
│   │   ├── java-core.cursorrules    # Java 핵심 규칙
│   │   ├── java-lombok.cursorrules  # Lombok 활용 규칙
│   │   ├── java-oop.cursorrules     # Java 객체지향 규칙
│   │   └── java-best-practices.cursorrules # Java 모범 사례
│   │
│   ├── python/
│   │   ├── python-core.cursorrules
│   │   ├── python-oop.cursorrules
│   │   └── python-best-practices.cursorrules
│   │
│   ├── typescript/
│   │   ├── typescript-core.cursorrules
│   │   ├── typescript-oop.cursorrules
│   │   └── typescript-best-practices.cursorrules
│   │
│   └── csharp/
│       ├── csharp-core.cursorrules
│       ├── csharp-oop.cursorrules
│       └── csharp-best-practices.cursorrules
│
└── frameworks/             # 프레임워크별 규칙 (향후 확장)
    ├── spring/
    ├── react/
    ├── angular/
    └── django/
```

## 🚀 사용 방법

### 1. 기본 설정
프로젝트 루트에 `.cursorrules` 파일을 생성하고 필요한 규칙들을 import:

```
# 공통 규칙 적용
@import "./cursor-rules/common/general.cursorrules"
@import "./cursor-rules/common/solid-principles.cursorrules"

# 언어별 규칙 적용 (Java 예시)
@import "./cursor-rules/languages/java/java-core.cursorrules"
@import "./cursor-rules/languages/java/java-lombok.cursorrules"
```

### 2. 프로젝트 규모별 적용

#### 🏠 소규모 프로젝트
```
@import "./cursor-rules/common/general.cursorrules"
@import "./cursor-rules/languages/java/java-core.cursorrules"
```

#### 🏢 중대규모 프로젝트
```
@import "./cursor-rules/common/general.cursorrules"
@import "./cursor-rules/common/solid-principles.cursorrules"
@import "./cursor-rules/common/design-patterns.cursorrules"
@import "./cursor-rules/languages/java/java-core.cursorrules"
@import "./cursor-rules/languages/java/java-lombok.cursorrules"
@import "./cursor-rules/languages/java/java-oop.cursorrules"
```

## 📋 규칙 카테고리 설명

### Common (공통 규칙)
- **general**: 기본적인 프로그래밍 원칙과 코드 작성 가이드
- **solid-principles**: SOLID 원칙 적용 가이드
- **design-patterns**: 자주 사용되는 디자인 패턴들
- **testing**: 테스트 작성 및 TDD 관련 규칙
- **naming-conventions**: 변수, 함수, 클래스 네이밍 규칙
- **code-quality**: 코드 품질 향상을 위한 규칙

### Languages (언어별 규칙)
각 언어별로 핵심 문법, 객체지향 특성, 모범 사례 등을 포함

## 🔧 커스터마이징

프로젝트 특성에 맞게 규칙을 선택적으로 적용하거나 수정하여 사용할 수 있습니다. 

# Planit - 개인 일정 관리 백엔드 API

**Planit**은 사용자가 자신의 일정을 효율적으로 관리할 수 있도록 돕는 개인 일정 관리 서비스의 백엔드 API입니다. 이 프로젝트는 Kotlin과 Spring Boot를 기반으로 구축되었으며, JWT를 이용한 소셜 로그인을 지원합니다.

## ✨ 주요 기능

- **사용자 관리**:
    - 소셜 로그인 (Google, Kakao, Naver)을 통한 간편한 회원가입 및 로그인
    - JWT 기반의 안전한 인증 시스템
    - 내 정보 조회
- **일정 관리**:
    - 일정 생성(Create), 조회(Read), 수정(Update), 삭제(Delete) (CRUD)
    - 월별/일별 일정 필터링 및 페이징 조회

## 🛠️ 기술 스택

- **언어**: `Kotlin 1.9.24`
- **프레임워크**: `Spring Boot 3.3.1`
- **데이터베이스**: `H2 In-memory DB`
- **인증**: `Spring Security`, `OAuth 2.0`, `JWT`
- **테스트**: `Kotest`, `MockK`
- **빌드 도구**: `Gradle`

## 🚀 시작하기

### 1. 사전 요구사항

- `JDK 17` 이상
- `Gradle 8.8` 이상 (프로젝트에 포함된 Gradle Wrapper 사용을 권장합니다)

### 2. 프로젝트 클론

```bash
git clone https://github.com/your-username/ganzi-blog.git
cd ganzi-blog
```

### 3. 환경 변수 설정

`src/main/resources/application.yml` 파일을 열어 아래 항목들을 실제 값으로 변경하거나, 환경 변수 또는 `application-local.yml` 파일을 생성하여 오버라이드해야 합니다.

```yaml
# OAuth2 Client Registration
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: [YOUR_GOOGLE_CLIENT_ID]
            client-secret: [YOUR_GOOGLE_CLIENT_SECRET]
          kakao:
            client-id: [YOUR_KAKAO_CLIENT_ID]
            client-secret: [YOUR_KAKAO_CLIENT_SECRET]
          naver:
            client-id: [YOUR_NAVER_CLIENT_ID]
            client-secret: [YOUR_NAVER_CLIENT_SECRET]

# JWT Secret Key
jwt:
  secret-key: "[YOUR_JWT_SECRET_KEY]" # 64바이트 이상의 복잡한 문자열을 권장합니다.
```

### 4. 애플리케이션 실행

프로젝트 루트 디렉토리에서 아래 명령어를 실행하여 애플리케이션을 시작합니다.

```bash
./gradlew bootRun
```

애플리케이션이 성공적으로 실행되면 `localhost:8080`에서 API 서버가 동작합니다.

## 📄 API 문서

API의 각 엔드포인트에 대한 상세한 명세는 아래 문서에서 확인할 수 있습니다.

- [**API 명세서 (api-spec.md)**](./docs/api-spec.md)

## ✅ 테스트

프로젝트의 모든 테스트 코드를 실행하려면 아래 명령어를 사용하세요.

```bash
./gradlew test
``` 