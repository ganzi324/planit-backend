# Step 4: 핵심 API 구현 (API Layer)

> **🎯 목표**: `api-spec.md` 명세에 따라 실제 동작하는 API 엔드포인트를 구현합니다. `@WebMvcTest`와 서비스 단위 테스트를 병행합니다.

---

### Sub-steps

- [x] **Sub-step 4.1: 일정 생성(Create) API 구현**
  - **Feature 브랜치**: `feature/create-schedule-api`
  - **예상 커밋**:
    1. `test(controller): 일정 생성 API 컨트롤러 테스트 추가`
    2. `test(service): 일정 생성 서비스 로직 테스트 추가`
    3. `feat(schedule): 일정 생성 API 구현`

- [x] **Sub-step 4.2: 일정 목록 조회(Read) API 구현**
  - **Feature 브랜치**: `feature/get-schedules-api`
  - **예상 커밋**:
    1. `test(controller): 일정 목록 조회 API 컨트롤러 테스트 추가`
    2. `feat(schedule): 일정 목록 조회 API 구현 (페이징/필터링 포함)`

- [x] **Sub-step 4.3: 일정 수정(Update) 및 삭제(Delete) API 구현**
  - **Feature 브랜치**: `feature/update-delete-schedule-api`
  - **예상 커밋**:
    1. `test(controller): 일정 수정/삭제 API 컨트롤러 테스트 추가`
    2. `feat(schedule): 일정 수정 및 삭제 API 구현`

- [x] **Sub-step 4.4: 사용자 정보 조회 API 구현**
  - **Feature 브랜치**: `feature/get-user-me-api`
  - **예상 커밋**:
    1. `test(controller): 내 정보 조회 API 컨트롤러 테스트 추가`
    2. `feat(user): 내 정보 조회 API 구현` 