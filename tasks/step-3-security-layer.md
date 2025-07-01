# Step 3: 사용자 인증/인가 계층 구현 (Security Layer)

> **🎯 목표**: Spring Security와 OAuth2를 사용하여 소셜 로그인을 구현하고, JWT를 통해 API 접근을 제어합니다.

---

### Sub-steps

- [x] **Step 3.1: Spring Security 및 OAuth2 기본 설정**
  - **Feature 브랜치**: `feature/security-oauth2-config`
  - **수행 내용**: SecurityFilterChain을 구성하여 정적 리소스 및 로그인 관련 URL 접근을 허용하고, 나머지 요청은 인증을 요구하도록 설정합니다.
  - **예상 커밋**: `feat(config): Spring Security 및 OAuth2 기본 설정`

- [x] **Sub-step 3.2: Custom OAuth2UserService 및 인증 성공 로직 구현**
  - **Feature 브랜치**: `feature/oauth2-user-service`
  - **수행 내용**: 소셜 로그인 성공 시, 사용자 정보를 받아 `UserRepository`를 통해 DB에 저장/업데이트하는 `CustomOAuth2UserService`를 구현합니다. 로그인 성공 후 JWT를 발급하는 핸들러를 추가합니다.
  - **예상 커밋**:
    1. `test(service): CustomOAuth2UserService 단위 테스트 추가`
    2. `feat(auth): CustomOAuth2UserService 및 인증 성공 핸들러 구현`

- [x] **Sub-step 3.3: JWT 검증 필터 구현**
  - **Feature 브랜치**: `feature/jwt-authentication-filter`
  - **수행 내용**: 클라이언트가 API 요청 시 헤더에 담아 보낸 JWT를 검증하고, 유효할 경우 Spring Security 컨텍스트에 인증 정보를 등록하는 필터를 구현합니다.
  - **예상 커밋**:
    1. `test(auth): JWT 유틸리티 및 검증 로직 테스트 추가`
    2. `feat(auth): JWT 검증 필터 구현 및 SecurityConfig 등록` 