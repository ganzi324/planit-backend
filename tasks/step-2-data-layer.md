# Step 2: 도메인 계층 구현 (Data Layer)

> **🎯 목표**: `domain-spec.md`를 바탕으로 데이터베이스와 직접 상호작용하는 엔티티와 리포지토리를 구현하고, `@DataJpaTest`를 통해 쿼리 동작을 검증합니다.

---

### Sub-steps

- [x] **Sub-step 2.1: 공통 엔티티 및 Enum 구현**
  - **Feature 브랜치**: `feature/common-entities-enums`
  - **수행 내용**: `BaseEntity`, `UserProvider` Enum, `SchedulePriority` Enum 클래스를 구현합니다.
  - **예상 커밋**: `feat: BaseEntity 및 공통 Enum 클래스 구현`

- [x] **Sub-step 2.2: User 엔티티 및 Repository 구현**
  - **Feature 브랜치**: `feature/user-entity-and-repo`
  - **수행 내용**: `User` 엔티티와 `UserRepository` 인터페이스를 구현하고, `@DataJpaTest`로 검증합니다.
  - **예상 커밋**:
    1. `test(repository): UserRepository 슬라이스 테스트 추가`
    2. `feat(domain): User 엔티티 및 Repository 구현`

- [ ] **Sub-step 2.3: Schedule 엔티티 및 Repository 구현**
  - **Feature 브랜치**: `feature/schedule-entity-and-repo`
  - **수행 내용**: `Schedule` 엔티티와 `ScheduleRepository` 인터페이스를 구현하고, `@DataJpaTest`로 검증합니다.
  - **예상 커밋**:
    1. `test(repository): ScheduleRepository 슬라이스 테스트 추가`
    2. `feat(domain): Schedule 엔티티 및 Repository 구현` 