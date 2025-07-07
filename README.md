# 📄 목차

1. [제작 배경](#1-제작-배경)
2. [요구사항 정의서 / 기능 정의서](#2-요구사항-정의서--기능-정의서)  
   - [개요](#개요--기존-koda-홈페이지의-문제점을-보완하고-사용자-중심의-ux를-고려하여-감성적-요소와-보안-응답성-접근성을-강화하는-방향으로-웹사이트를-리뉴얼)
   - [기존 홈페이지 문제점](#기존-koda-홈페이지-문제점)
   - [개선 목표 및 주요 기능](#개선-목표-및-주요-기능)
   - [기대 효과](#기대-효과)
3. [기술 스택](#3-기술-스택)
4. [팀원 R&R(Role & Responsibility)](#4-팀원-rrrole--responsibility)
5. [API 명세서](#5-api-명세서)
6. [폴더 구조](#6-폴더-구조)
7. [RFP 필수 구현 개발 기능](#7-rfp-필수-구현-개발-기능)
8. [RFP 이외의 개발 기능](#8-rfp-이외의-개발-기능)
9. [Branch Strategy](#9-branch-strategy)
10. [생성된 브랜치 리스트 (2025-06-18)](#10-생성된-브랜치-리스트-2025-06-18)

---

## 1. 제작 배경
- **엘리먼트소프트 : KODA 공식홈페이지 개발** (보건복지부 산하 기관)
- KODA(한국 장기 조직 기능원)페이지를 통해 사용자의 데이터를 안전하게 처리하고 홈페이지 유입과 리텐션을 높이는 개발 프로젝트를 진행
- 전자정부프레임워크 사이트의 웹접근성과 사용자 경험을 높이는 새로운 공공 서비스 웹사이트를 구축하여 진행
<br>

## 2. 요구사항 정의서 / 기능 정의서
### 개요 : 기존 Koda 홈페이지의 문제점을 보완하고, 사용자 중심의 UX를 고려하여 감성적 요소와 보안, 응답성, 접근성을 강화하는 방향으로 웹사이트를 리뉴얼
### 기존 Koda 홈페이지 문제점
| 항목     | 내용                                           |
| ------ | -------------------------------------------- |
| UI/UX  | 정보 위주 구성으로 정서적 배려 부족<br>감성적 요소나 유가족 공감 요소 미흡 |
| 운영 구조  | PC와 모바일 버전 분리 운영으로 인한 관리 비효율                 |
| 보안     | 게시글/댓글 입력 시 CAPTCHA 미적용 등 보안 노출              |
| 조직도 기능 | 조직도는 정적 텍스트 제공, 실시간 연락처 탐색/응답 기능 없음          |
### 개선 목표 및 주요 기능
| 핵심 목표          | 구현 사항                                                       |
| -------------- | ----------------------------------------------------------- |
| **유가족을 위한 공간** | 정서적 안정감을 주는 색상·이미지 구성<br>추모 공간, 수혜자 인터뷰 등 감성적 연결 요소 추가      |
| **반응형 웹 구현**   | PC/모바일/태블릿 환경에서 동일한 UX 제공<br>반응형 UI 적용                      |
| **보안 강화**      | CAPTCHA 등 보안 기능 도입                                          |
| **조직도 챗봇 도입**  | 챗봇을 통한 조직도 탐색 및 담당자 안내<br>예: “OO업무 담당자 전화번호 알려줘”와 같은 자연어 응답 |
### 기대 효과
- 유가족 공감 공간 제공으로 감정적 만족도 향상
- 반응형 설계로 다양한 기기에서 접근성 개선
- 보안 기능 강화로 사용자 신뢰도 확보
- 실시간 챗봇 응답을 통한 조직 접근성 및 업무 효율 증가
<br>



## 3. 기술 스택
- Spring Boot : 2.7.18
- Java : 17
- JPA
- MySQL
- 기술 스택 선정 이유
    - Spring Boot / Java / MySQL : 기업 요청
    - JPA: 생산성 향상 기대, 반복 쿼리 방지 목적
<br>


## 4. 팀원 R&R(Role & Responsibility)

| 이름   | 직급/직책        | 책임 파트 | 역할 | 책임 | 상세 업무 |
|--------|------------------|------------|------|------|-----------|
| **김예린** | BE Leader         |   백엔드 / API 개발 / AI 모델 학습 / 시각화 / 보안 |   감정 분석 AI 개발 및 관리자 기능 구현    |   기술 리더   |     - 감정 판단 AI 모델 학습 및 추론 AOP 개발 <br> - 비난 글 관련 저장 DB 모델링  <br> - 비난 글 정렬/필터링 <br> - Thymleaf를 이용한 관리자 페이지 구현 <br> - Chart.js를 활용한 통계 시각화 <br> - 비난 게시글 관리 기능 개발 <br> - Spring Security를 이용한 관리자 인증·인가 설정      |
| **한국진** | Product Manager | 로깅 / 인프라 / 배포 자동화 / 홈 화면 API | 인프라 환경 및 시스템 통합 관리 | 총괄 기획자 | - Logback + 커스텀 Appender를 활용하여 MySQL에 로그 적재<br> - ELK 파이프라인을 통해 로그 데이터를 Elasticsearch에 전송하고 Kibana로 시각화 구성<br> - 요청 주소/내용/IP 기반 사용자 사용 로그 추적 및 관리<br> - FastAPI, Spring Boot, Logstash, MySQL, Kibana, Elasticsearch를 Docker로 컨테이너화하여 실행 환경 일관성 확보<br> - docker-compose로 컨테이너 일괄 관리 및 네트워크 연결 설정<br> - GitHub Actions를 통한 CI/CD 구성<br>   · PR 발생 시 Spring Boot 프로젝트 자동 빌드 및 테스트<br>   · main 브랜치 병합 시 자동 배포 수행<br> - 메인 페이지에서 필요한 게시판별 데이터를 위한 API 구성<br> - JPQL 기반 데이터 조회 쿼리 작성 |
| **곽우진** | Team Member | 백엔드 / API 개발             | 기증후 스토리 / 추모관 / 공통 모듈 | 기능 구현 | - 기증후 스토리: JPA 기반 CRUD, 익명 처리 및 댓글 포함 여부에 따른 DTO 분리<br>- 스토리 댓글: 비회원 댓글 기능, CAPTCHA 인증, 금칙어 필터링<br>- 기증자 추모관: 추모글/댓글 CRUD 및 정렬(등록일/생일/나이) 기능 구현<br>- 공지사항: CRUD, 파일 업/다운로드, 조회수 증가, 페이징 처리<br>- 공통 처리: CAPTCHA 검증, 이름 마스킹/날짜 포맷/성별 변환 어노테이션, ResponseBodyAdvice 기반 응답 후처리 (DTO 리플렉션 + 캐시 처리) |
| **김예지** | Team Member       | 백엔드 / API 개발 | 하늘나라 편지 & 수혜자 편지 기능 개발 | 기능 구현 | - JPA 기반 도메인 중심 설계로 CRUD API 구현<br> - 편지 등록·조회·수정·삭제 시 비밀번호 인증 및 소프트 삭제(delFlag) 로직 적용<br> - 제목·내용·기증받은 장기·기증년도 기반 동적 검색 기능을 JPQL / Specification으로 구현<br> - 기증자 조회 기능을 별도 엔드포인트로 분리해 검색 기능 제공<br> - 공통 ApiResponse 포맷으로 일관된 응답 처리 |
| **김나현** | Team Member | 백엔드 / API 개발 / AI 모델 학습 | 조직도 챗봇 기능 전체 개발 | 기능 구현 | - 조직도 챗봇 백엔드 API 개발(Spring + Python 연동)<br> - Spring: WebClient를 활용한 비동기 논블로킹 처리 구조 구현<br> - 요청/응답 DTO를 불변 객체로 설계하여 안정성 확보<br> - 응답 포맷을 Mono<ResponseEntity<ChatApiResponse>> 형태로 통일<br> - onErrorResume을 통한 예외 핸들링 및 로깅 처리<br> - Python: Few-shot 프롬프트 기반 질문 요약 및 유형 분류, Zero-shot 프롬프트로 최종 응답 생성<br> - Parent Document Retriever 로직 생성 및 챗봇 응답 생성<br> - ApiResponse 포맷으로 통일된 응답 제공<br> - 비동기 응답 및 전역 예외 처리 + 로깅 처리 구조 설계 |
| **김우주** | Team Member | 백엔드 / API 개발 | 디지털 추모 공간 및 관리자 기능 구현 | 기능 구현 | - '모든 기증자'를 위한 디지털 추모 공간 기획 및 백엔드 기능 구현<br> - 사용자 추모 메시지(꽃/나뭇잎) 등록 및 조회 API 개발<br> - 관리자 기능 전반(계정, 메시지, 로그 관리) API 개발<br> - AOP 기반 관리자 등급별 API 접근 제어 로직 적용<br> - Querydsl 도입으로 관리자/사용자 검색 조건에 따른 동적 조회 구현<br> - Spring Interceptor를 활용한 세션 기반 인증 처리<br> - BCrypt로 비밀번호 단방향 해싱 및 검증 처리<br> - 로그인 성공 시 IP, 시간 등 로그인 이력을 MySQL에 저장 <br> **백엔드**: https://github.com/space-999/Memorial-BE.git<br> **프론트엔드(사용자)**: https://github.com/space-999/Memorial-user-FE.git<br> **프론트엔드(관리자)**: https://github.com/space-999/Memorial-admin_FE.git|
| **이병우** | Team Member  | 백엔드 / AI 모델 연동 / 추천 시스템 | 편지 기반 감정 분석 및 꽃 추천 시스템 구현 | 기능 구현 | - 하늘나라편지/기증후스토리/수혜자 편지 내용 기반 감정 분석 (Zero-shot, RoBERTa KoNLI)<br>- 감정에 어울리는 꽃 추천 API 구현<br>- 꽃 정보 등록/조회/수정/삭제 기능 구현<br>- AWS S3를 활용한 꽃 사진 업로드 및 출력 <br> **프론트엔드(관리자)** : https://github.com/ByeongWoo99/Admin_Flower_Service  <br> **백엔드** : https://github.com/ByeongWoo99/Flower_Service|
<br>



## 5. API 명세서
- 기본적으로 기업에서 제공한 [API 명세서](https://www.notion.so/junesoft/API-1bbbb9ead439800299f0ffe08ec5fe33)를 기반으로 개발
- 추가 API: 챗봇 관련 API
- 수정 API: 페이징 객체 정보(Page)의 형식으로 반환하는 Response
- [최종 API 정리](https://www.notion.so/API-21bcc686750c80bd83e9eca9acadb11e)
<br>



## 6. 폴더 구조

```
- infra/                       # (internal) 인프라 관련 디렉토리
  └─ logstash/                 # (internal) 로그 수집 및 분석을 위한 Logstash 설정

- spring/                      # Spring Boot 백엔드 프로젝트
  ├─ gradle/wrapper/           # Gradle 빌드 도구 설정
  ├─ src/
  │  ├─ main/
  │  │  ├─ java/com/ttasum/memorial/
  │  │  │  ├─ annotation/      # 커스텀 어노테이션
  │  │  │  ├─ aop/             # AOP 설정 및 로깅 처리
  │  │  │  ├─ config/          # Spring 설정 파일 (Security, WebMvc 등)
  │  │  │  ├─ controller/      # API 요청 처리 컨트롤러
  │  │  │  ├─ domain/          # 도메인 계층
  │  │  │  │  ├─ entity/       # JPA 엔티티 클래스
  │  │  │  │  └─ repository/   # JPA 리포지토리 인터페이스
  │  │  │  ├─ dto/             # 요청 및 응답 DTO
  │  │  │  ├─ exception/       # 예외 및 예외 처리 클래스
  │  │  │  ├─ logging/         # 커스텀 Logback Appender 등 로깅 관련
  │  │  │  ├─ service/         # 비즈니스 로직 서비스 계층
  │  │  │  └─ util/            # 유틸리티 클래스
  │  │  └─ resources/          # 설정 및 정적 리소스 (application.yml, logback.xml 등)
  │
  └─ test/                     # 테스트 코드

- python/                      # Python 기반 AI 모델 관련 코드
  ├─ forbiddenWord/            # (internal) 금칙어(비속어/욕설) 판별 모델(모델: monologg/koelectra-base-discriminator)
  │                        
  ├─ predictEmotion/           # (internal) 감정 분석 모델 (모델: klue/roberta-base, 데이터: KOTE 기반 fine-tuning)
  │                        
  └─ chat/                     # 챗봇 서비스용 대화형 AI 모델 (모델: Upstage Solar, 내부 DB 기반 QA 챗봇)
                       
```

<br>


## 7. RFP 필수 구현 개발 기능

- **곽우진**: 기증후 스토리 / 추모관 / 공지사항 기능 구현 (CRUD, 댓글/정렬 기능 포함)
- **김예지**: 하늘나라/수혜자 편지 등록·수정·삭제 기능 구현 및 기증자 조회 API 구현
- **김나현**: 조직도 챗봇 백엔드 + Python 연동 API 개발, WebClient + 비동기 예외처리 구성
- **한국진**: 로깅 시스템, Dockerizing, CI/CD 구성, 메인페이지 API 설계 및 JPQL 조회 구현
<br>

> 본 프로젝트는 실제 RFP 요구사항을 충실히 분석하고, 필수 구현 기능은 최소한의 인력으로도 충분히 완성 가능하다고 판단함
> 이에 따라 일부 인력은 Overstaffing을 방지하고, 리소스를 효율적으로 활용하기 위해  
> 기업에 추가적인 가치를 줄 수 있는 기능 (AI 분석, 챗봇, 추천 시스템 등)을 자율 기획·설계·개발함
> 이러한 작업은 실무성 강화 및 서비스 확장성 측면에서도 긍정적인 결과를 도출함

## 8. RFP 이외의 개발 기능
- **김예린**: 관리자 감정 분석 AI 연동(AOP 기반), 통계 시각화 기능, 관리자 인증·인가 보안 설정 구현
- **김우주**: 모든 기증자를 위한 디지털 추모 공간 기획 및 개발, 관리자 기능 전반 구축, 로그인 추적 및 세션 인증 설계
- **이병우**: 편지 기반 감정 분석 모델 연동 및 감정 기반 꽃 추천 시스템 API, AWS S3 기반 이미지 처리
<br>


## 9. Branch Strategy

| 브랜치명          | 용도                           | 비고                                                    |
|------------------|--------------------------------|----------------------------------------------------------|
| `main` (또는 `release`) | 최종 배포본                       | 프로덕션 서버와 동기화                                     |
| `develop`        | 기능 통합용 개발 브랜치              | QA 및 스테이징 서버 대상                                   |
| `develop-internal` | 추가 기능 개발 브랜치                | 기업 연계와 무관한 기능 개발. 사용 방식은 `develop`과 동일          |
| `feature/*`      | 기능 단위 구현 브랜치                | 예: `feature/login-api`                                  |
| `fix/*`          | 버그 수정 브랜치                   | 예: `fix/invalid-token`                                  |
| `qa`             | QA 전용 브랜치                   | QA 팀에서 테스트 시 사용                                  |
| `staging`        | 시연/컨펌 전용 중간 서버용 브랜치        | 클라이언트/내부 시연에 사용                                |
| `hotfix/*`       | 운영 긴급 수정 브랜치                | 바로 `main`에 머지됨                                     |
<br>


## 10. 생성된 브랜치 리스트 (2025-06-18)
  - * main : 최종 배포본
  - develop : RFP를 기반으로 개발한 기능 통합용 개발 브랜치
    - feature/TTASUM-84-org-chart-chatbot : 챗봇 AI 기능 / API 개발
    - feature/TTASUM-83-donationLetters : 기증후 스토리 API 개발
    - feature/TTASUM-88-heavenLetters-service : 하늘나라 편지 API 개발
    - feature/TTASUM-95-donor-notice-view : 기증자 추모관 & 공지사항 API 개발
    - feature/TTASUM-114-FE-main : 홈 화면 API 개발
    - feature/TTASUM-117-recipientLetters-service : 수혜자 편지 API 개발
    - feature/TTASUM-111-logging-mysql : 로그 DB 저장

  - develop-internal : 필수 기능과 추가적인 기능 통합용 개발 브랜치
    - feature/TTASUM-112-admin-blameText : 글 의도 감정 AI 기능 / AOP 개발
    - feature/TTASUM-86-forbidden-word-check-service : 비난 글 판단 AI 기능 / AOP 개발 **(deprecated)**
    - feature/TTASUM-121-git-action-CI-CD : Git-Action Ci/CD 파이프라인
    - feature/TTASUM-116-profanity-filtering : 비난 단어 필터링 **(deprecated)**
    - feature/TTASUM-108-flower-service : 편지 감정 분석 AI 기능 / 감정에 맞는 꽃 추천 기능 / API 개발 
    - feature/TTASUM-106-logging : 로깅 ELK 파이프라인

  - hotfix : 개발 도중 설정/운영 관련 전체적인 수정 브랜치
    - hotfix/TTASUM-115-env-security : DB URL, ID 환경설정 보안
    - hotfix/TTASUM-118-donation-package-renaming : 패키지명 Donation -> donation 으로 변경
    - hotfix/TTASUM-119-donation-conflict-solve : 기증자 관련 conflict 해결 **(deprecated)**
    - hotfix/TTASUM-87-update-DB-properties : DB pw 환경설정 보안
    - hotfix/TTASUM-96-change-root-directory : repository root 경로 변경



  
  
  
  
