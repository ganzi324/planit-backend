# 도메인 모델 상세 설계

이 문서는 PlanIt 프로젝트의 JPA 엔티티와 도메인 모델의 세부 설계를 정의합니다.
MVP 단계에서는 **JPA 엔티티와 도메인 엔티티를 통합한 모델**을 사용합니다.

---

## 1. 공통 베이스 엔티티 (`BaseEntity`)

모든 엔티티는 공통적으로 ID와 생성/수정 시간을 갖습니다. 코드 중복을 피하기 위해 `BaseEntity`를 추상 클래스로 정의하고, 다른 엔티티들이 이를 상속받습니다.

*   **`id`**: `Long`, PK, 자동 생성 (Auto-increment)
*   **`createdAt`**: `LocalDateTime`, Not Null, 생성 시 자동 설정
*   **`updatedAt`**: `LocalDateTime`, Not Null, 수정 시 자동 설정

---

## 2. `User` 엔티티

OAuth2를 통해 인증된 사용자 정보를 관리합니다.

| 필드명      | 타입                  | 제약조건                        | 설명                                  |
| :---------- | :-------------------- | :------------------------------ | :------------------------------------ |
| `id`        | `Long`                | PK (BaseEntity)                 | 사용자 고유 식별자                    |
| `name`        | `String`              | Not Null, `length=100`          | 사용자 이름                           |
| `email`       | `String`              | Not Null, `length=255`, Unique  | 사용자 식별용 이메일                  |
| `provider`    | `UserProvider` (Enum) | Not Null                        | OAuth2 제공자 (GOOGLE, GITHUB)        |
| `schedules` | `List<Schedule>`      | 1:N 관계                        | 해당 유저가 작성한 모든 일정을 참조   |
| `createdAt`   | `LocalDateTime`       | Not Null (BaseEntity)           | 생성 시간                             |
| `updatedAt`   | `LocalDateTime`       | Not Null (BaseEntity)           | 수정 시간                             |

### `UserProvider` Enum
*   `GOOGLE`
*   `GITHUB`

---

## 3. `Schedule` 엔티티

사용자가 등록하고 관리하는 개별 일정 정보를 담습니다.

| 필드명                 | 타입                  | 제약조건                             | 설명                                  |
| :------------------- | :-------------------- | :----------------------------------- | :------------------------------------ |
| `id`                 | `Long`                | PK (BaseEntity)                      | 일정 고유 식별자                      |
| `user`               | `User`                | Not Null, FK (N:1 관계)              | 일정을 소유한 사용자                  |
| `title`              | `String`              | Not Null, `length=255`               | 일정 제목                             |
| `description`        | `String`              | Nullable, `TEXT` type                | 상세 설명 (길이 제한 없음)            |
| `priority`           | `Priority` (Enum)     | Not Null                             | 중요도 (`HIGH`, `MEDIUM`, `LOW`)      |
| `isCompleted`        | `Boolean`             | Not Null, `default=false`            | 완료 여부                             |
| `startDate`          | `LocalDateTime`       | Not Null                             | 시작 시간                             |
| `endDate`            | `LocalDateTime`       | Not Null                             | 종료 시간                             |
| `alarmOffsetMinutes` | `Integer`             | Nullable                             | 미리 알림 시간(분), MVP에선 값만 저장 |
| `createdAt`            | `LocalDateTime`       | Not Null (BaseEntity)                | 생성 시간                             |
| `updatedAt`            | `LocalDateTime`       | Not Null (BaseEntity)                | 수정 시간                             |

### `Priority` Enum
*   `HIGH`
*   `MEDIUM`
*   `LOW`

### 엔티티 내 비즈니스 규칙
엔티티 스스로 데이터의 일관성을 유지하도록 다음 로직을 포함합니다.

*   **`validateDates()`**: `endDate`가 `startDate`보다 이전일 경우 `IllegalArgumentException`을 발생시키는 내부 로직. (생성자 또는 `init` 블록에서 호출)
*   **`complete()`**: `isCompleted` 상태를 `true`로 변경. 이미 `true`일 경우 예외 발생.
*   **`uncomplete()`**: `isCompleted` 상태를 `false`로 변경. 이미 `false`일 경우 예외 발생. 