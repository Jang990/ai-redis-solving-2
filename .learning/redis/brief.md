# S0 브리프 — 마켓 MVP: 상품 상세가 곧 매출이다

## 상황
사이드 프로젝트로 시작한 이커머스 **"요즘마켓"** 이 시드 투자를 받았다. 대표가 첫 스프린트 티켓을 던졌다:
"상품 등록하고, 상세 페이지 보여주고, 리뷰 평점·개수랑 조회수가 보여야 해요. **조회수는 셀러들이
제일 자주 보는 숫자**라 정확해야 하고, **가격 바꾸면 다음 조회에 바로 반영**돼야 해요 — 지난주에
가격 실수 반영 늦어서 셀러 항의 받았어요." 아직 유저는 하루 몇백 명. 기술 선택의 고통은 없다.
정직하게 동작하는 것부터.

## 목표
`app/src/test/java/harness/ProductApiSpec.java` **전부 green.** 행동 계약(JSON 필드명 포함):

| API | 계약 |
|---|---|
| `POST /products` `{name, price, category}` | 2xx, 응답에 `id` 포함 |
| `GET /products/{id}` | `{id, name, price, category, viewCount, reviewCount, avgRating}` · 없는 상품 404 |
| (조회수) | `viewCount` = 해당 조회를 **포함**한 누적 조회수 (5번 조회하면 마지막 응답이 5) |
| `POST /products/{id}/reviews` `{rating(1~5), content}` | 2xx · 상세의 `reviewCount`/`avgRating`에 반영 |
| `PATCH /products/{id}` `{price}` | 2xx · **다음 조회에 즉시 반영** |

내부 구현(스키마·계층·쿼리 방식)은 전부 네 선택이다. 테스트는 HTTP 행동만 본다.

## 준비 (1회)
1. `docker compose up -d` — mysql(**localhost:3307**, root/root, db `app`) 등이 뜬다. (네 PC의 로컬 mysqld가 3306을 쓰고 있어 3307로 노출 — 학습은 반드시 compose의 mysql로.) 지금 스테이지에서 쓰는 건 mysql뿐. 나머지 컨테이너는 앞으로의 세계 일부이니 신경 끄기.
2. `app/` 폴더에 Spring Boot 프로젝트 생성(Java 21, Gradle, web·jpa·mysql·lombok 권장 + actuator·`micrometer-registry-prometheus` — 관측 스택용, 아래 3). `app/Dockerfile`은 이미 있음(나중 단계용, 건드리지 말 것). 테스트 파일도 이미 `app/src/test/...`에 있음 — 프로젝트를 그 위에 생성하면 됨.
3. `application.yml`에 관측 배선 두 줄(측정 도구용, 답 아님):
   `management.endpoints.web.exposure.include: prometheus,health`
   `management.metrics.distribution.percentiles-histogram.http.server.requests: true`
4. 앱은 호스트에서 `./gradlew bootRun`(8080). 테스트는 `BASE_URL` env(기본 `http://localhost:8080`)로 실행 중인 앱을 친다 — 앱 켜고 `./gradlew test`.

## 측정
아직 없음. curl + 테스트 출력이 전부. (grafana http://localhost:3000 은 다음부터 의미가 생긴다.)

## escalation
green 후 세계에 시간이 흐른다. 다음 사건은 **성장의 얼굴**로 온다 — 내용 비공개.

## 멈춤
전부 green이면 코치 호출. 또는 서로 다른 시도 2개가 같은 증상으로 실패하면 코치 호출.
