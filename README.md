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