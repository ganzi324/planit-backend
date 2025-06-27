# API 명세서 (API Specification)

이 문서는 PlanIt 프로젝트의 백엔드 API 엔드포인트를 상세히 정의합니다. 모든 API 요청은 인증된 사용자를 기준으로 동작합니다.

---

## 1. 공통 사항

### 1.1 인증

*   모든 API는 요청 헤더에 `Authorization: Bearer <JWT_TOKEN>`을 포함해야 합니다. (OAuth2 로그인 성공 시 발급)

### 1.2 페이징 응답 형식

일정 목록 조회와 같이 여러 데이터를 반환하는 API는 다음과 같은 표준 페이징 응답 형식을 사용합니다.

```json
{
  "content": [
    // 실제 데이터 배열 (예: ScheduleResponse 객체 리스트)
  ],
  "pageable": {
    "pageNumber": 0,    // 현재 페이지 번호 (0부터 시작)
    "pageSize": 10,     // 페이지 당 항목 수
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,      // 전체 페이지 수
  "totalElements": 48,  // 전체 항목 수
  "last": false,        // 마지막 페이지 여부
  "size": 10,
  "number": 0,
  "sort": { /* ... */ },
  "numberOfElements": 10, // 현재 페이지의 항목 수
  "first": true,        // 첫 페이지 여부
  "empty": false
}
```

---

## 2. 인증 및 사용자 API

### 2.1 OAuth2 로그인

*   **URL**: `GET /oauth2/authorization/{provider}` (provider: `google`, `github`)
*   **설명**: Spring Security OAuth2 클라이언트가 처리하는 엔드포인트입니다. 프론트엔드에서 해당 URL로 사용자를 리디렉션하면 소셜 로그인 과정이 시작됩니다. 별도의 컨트롤러 구현은 필요하지 않습니다.

### 2.2 로그인한 사용자 정보 조회

*   **URL**: `GET /api/users/me`
*   **설명**: 현재 인증된 사용자의 정보를 반환합니다.
*   **응답 (200 OK)**: `UserResponse` DTO

```json
{
  "name": "홍길동",
  "email": "user@example.com"
}
```

---

## 3. 일정 (Schedule) API

### 3.1 일정 목록 조회 (페이징 및 필터링)

*   **URL**: `GET /api/schedules`
*   **설명**: 사용자의 일정 목록을 조건에 따라 페이징하여 조회합니다.
*   **쿼리 파라미터**:
    *   `page`: 페이지 번호 (0부터 시작, 기본값: 0)
    *   `size`: 페이지 당 항목 수 (기본값: 10)
    *   `sort`: 정렬 기준 (예: `startDate,desc`, 기본값: `startDate,asc`)
    *   `isCompleted`: 완료 여부 필터링 (`true` / `false`)
    *   `priority`: 중요도 필터링 (`HIGH`, `MEDIUM`, `LOW`)
*   **응답 (200 OK)**: [공통 페이징 응답 형식](#12-페이징-응답-형식) (content는 `ScheduleResponse` DTO 배열)

### 3.2 일정 생성

*   **URL**: `POST /api/schedules`
*   **설명**: 새로운 일정을 생성합니다.
*   **요청 본문**: `ScheduleRequest` DTO
    ```json
    {
      "title": "새로운 회의",
      "startDate": "2024-08-15T14:00:00",
      "endDate": "2024-08-15T15:00:00",
      "priority": "HIGH",
      "alarmOffsetMinutes": 30
    }
    ```
*   **응답 (201 Created)**: 생성된 일정 정보 (`ScheduleResponse` DTO)

### 3.3 단일 일정 조회

*   **URL**: `GET /api/schedules/{scheduleId}`
*   **설명**: 특정 ID를 가진 일정의 상세 정보를 조회합니다.
*   **응답 (200 OK)**: `ScheduleResponse` DTO

### 3.4 일정 수정

*   **URL**: `PUT /api/schedules/{scheduleId}`
*   **설명**: 특정 일정의 전체 정보를 수정합니다.
*   **요청 본문**: `ScheduleRequest` DTO
*   **응답 (200 OK)**: 수정된 일정 정보 (`ScheduleResponse` DTO)

### 3.5 일정 완료/미완료 처리

*   **URL**: `PATCH /api/schedules/{scheduleId}/complete`
*   **설명**: 특정 일정의 완료 상태만 수정합니다.
*   **요청 본문**:
    ```json
    {
      "isCompleted": true
    }
    ```
*   **응답 (200 OK)**: 수정된 일정 정보 (`ScheduleResponse` DTO)

### 3.6 일정 삭제

*   **URL**: `DELETE /api/schedules/{scheduleId}`
*   **설명**: 특정 일정을 삭제합니다.
*   **응답 (204 No Content)**: 성공 시 응답 본문 없음. 