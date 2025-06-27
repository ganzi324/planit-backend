# 프로젝트 개요: PlanIt (계획 기반 일정 관리 도구)

## 🎯 목표

개인의 일정을 효과적으로 관리하고, 계획적인 생활 습관을 형성할 수 있도록 돕는 웹 서비스.

* Google/GitHub OAuth 로그인 기능을 통해 사용자 식별
* 일정을 등록하고 완료 상태를 관리
* 하루 단위로 현재 및 다음 일정을 강조하여 시각화
* 중요도 기반 일정 구분 및 필터링 제공

---

## 🧩 주요 기능 (MVP)

### 1. 사용자 인증

* Google / GitHub OAuth2 로그인
* 로그인한 사용자만 자신의 일정을 볼 수 있음

### 2. 일정 등록/수정/삭제

* 제목 (필수)
* 시작일, 종료일 (필수)
* 설명 또는 메모 (선택)
* 중요도 (필수): HIGH / MEDIUM / LOW
* 미리 알림 시간 설정 (선택): 알림 전 offset (예: 10분 전) → 저장만, 알림은 추후 구현

### 3. 일정 완료/완료 취소 기능

* 사용자는 일정을 완료 처리할 수 있음 (`is_completed = true`)
* 완료된 일정은 시각적으로 구분 (예: 회색 처리 또는 취소선)
* 완료된 일정은 다시 미완료로 변경 가능

### 4. 일정 목록 보기

* 하루 단위로 일정 조회 (기본 뷰)
* 중요도 필터: HIGH / MEDIUM / LOW
* 완료 여부 필터: 완료 / 미완료 / 전체
* 시작일 기준 정렬

### 5. 대시보드 뷰

* 현재 진행 중인 일정 강조 (시작 <= now <= 종료)

  * 표시 예: 🟢 현재 진행 중
* 다음 예정된 일정 강조 (now 이후 가장 가까운 시작일 기준)

  * 표시 예: 🔜 다음 일정

---

## 🏗️ 향후 확장 기능

| 분류      | 기능 설명                                      |
| ------- | ------------------------------------------ |
| 알림 기능   | 알림 시간 설정 기반 푸시/이메일 발송 구현 (알림 큐, cron 등 활용) |
| 회고 및 평가 | 주간/월간 일정 달성률 및 개인 회고 작성 기능                 |
| 통계 대시보드 | 완료율, 중요도별 소요 시간 통계 시각화                     |
| 협업 기능   | 일정 공유, 팀 일정, 공동 작성 기능                      |
| UI 개선   | 달력 뷰(주간/월간), 드래그 앤 드롭, 일정 검색 기능            |

---

## 🧱 기본 ERD 설계

```plaintext
User
- id (PK)
- email
- name
- provider (GOOGLE, GITHUB)
- created_at

Schedule
- id (PK)
- user_id (FK)
- title
- description
- start_date (datetime)
- end_date (datetime)
- priority (ENUM: HIGH, MEDIUM, LOW)
- is_completed (BOOLEAN)
- alarm_offset_minutes (INTEGER, nullable)
- created_at
- updated_at
```

---

## 🔧 기술 스택 제안

* **프론트엔드**: React + TailwindCSS 또는 Next.js
* **백엔드**: Spring Boot (Java or Kotlin)
* **DB**: PostgreSQL
* **OAuth2 인증**: Spring Security + Google/GitHub Provider
* **빌드 및 배포**: Docker + GitHub Actions (CI/CD), Fly.io 또는 Railway

---

## ⏳ 일정 제안 (MVP)

| 주차  | 목표                            |
| --- | ----------------------------- |
| 1주차 | 기획 확정, DB 모델링, OAuth 인증 구현    |
| 2주차 | 일정 등록/조회/수정/삭제 API 구현 및 화면 개발 |
| 3주차 | 완료 기능, 중요도 필터링, 대시보드 화면 구현    |
| 4주차 | 버그 수정, 테스트, 배포                |

---

## ✅ 마무리

PlanIt은 MVP 단계에서 "개인의 일정 등록 + 실시간 하이라이트"라는 명확한 목표에 집중합니다.
추후에는 습관 형성, 협업 기능, 통계 시각화 등으로 점진적 확장을 고려할 수 있습니다.
