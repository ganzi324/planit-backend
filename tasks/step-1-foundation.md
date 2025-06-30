# Step 1: 프로젝트 기반 설정 (Foundation)

> **🎯 목표**: 실제 코드를 작성하기 전에, 프로젝트의 뼈대를 구성하고 모든 개발자가 동일한 환경에서 시작할 수 있도록 설정합니다. 이 단계는 `develop` 브랜치에서 직접 진행하거나 `chore/initial-setup`과 같은 별도 브랜치에서 진행할 수 있습니다.

---

### Sub-steps

- [x] **Sub-step 1.1: Gradle 프로젝트 생성 및 의존성 설정**
  - **수행 내용**: Spring Initializr를 통해 `tech-stack.md`에 명시된 Kotlin, Spring Boot 버전으로 Gradle 프로젝트를 생성하고, 필요한 모든 의존성을 `build.gradle.kts`에 추가합니다.
  - **예상 커밋**: `feat: 프로젝트 초기 설정 및 의존성 추가`

- [x] **Sub-step 1.2: 기본 패키지 구조 및 설정 파일 구성**
  - **수행 내용**: `controller`, `service`, `repository`, `domain`, `dto`, `config` 등 표준 패키지 구조를 생성하고, `application.yml`에 H2 데이터베이스 및 OAuth2 관련 기본 설정을 추가합니다.
  - **예상 커밋**: `feat: 기본 패키지 구조 및 yml 설정 파일 구성` 