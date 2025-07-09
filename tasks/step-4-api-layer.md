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

- [x] **Sub-step 4.5: 일정 완료/미완료 처리 API 구현**
  - **Feature 브랜치**: `feature/complete-schedule-api`
  - **예상 커밋**:
    1. `test(controller): 일정 완료 처리 API 컨트롤러 테스트 추가`
    2. `feat(schedule): 일정 완료 처리 API 구현`

- [x] **Sub-step 4.6: 일정 목록 상세 필터링 기능 추가**
  - **Feature 브랜치**: `feature/filter-schedules-api`
  - **예상 커밋**:
    1. `test(controller): 일정 목록 상세 필터링 테스트 추가`
    2. `refactor(schedule): 일정 목록 조회 API에 상세 필터링 기능 추가`

- [ ] **Sub-step 4.7: 단일 일정 조회 API 구현**
  - **Feature 브랜치**: `feature/get-single-schedule-api`
  - **API 명세**: `GET /api/schedules/{scheduleId}` (api-spec.md 정의됨)
  - **예상 커밋**:
    1. `test(controller): 단일 일정 조회 API 컨트롤러 테스트 추가`
    2. `test(service): 단일 일정 조회 서비스 로직 테스트 추가`
    3. `feat(schedule): 단일 일정 조회 API 구현`

- [ ] **Sub-step 4.8: 대시보드 뷰 API 구현 (MVP 핵심 기능)**
  - **Feature 브랜치**: `feature/dashboard-view-api`
  - **API 명세**: `GET /api/schedules/dashboard` (신규 추가 필요)
  - **핵심 기능**:
    - 🟢 **현재 진행 중인 일정**: 시작일 <= 현재시간 <= 종료일
    - 🔜 **다음 예정된 일정**: 현재시간 이후 가장 가까운 시작일 기준
    - ⏰ **오늘의 일정**: 시작일이 오늘인 일정들
  - **예상 커밋**:
    1. `docs(api): 대시보드 뷰 API 명세 추가`
    2. `test(controller): 대시보드 뷰 API 컨트롤러 테스트 추가`
    3. `test(service): 대시보드 뷰 서비스 로직 테스트 추가`
    4. `feat(schedule): 대시보드 뷰 API 구현 (현재/다음 일정 강조)` 