# 진행 상태 — redis

## 현재
- 스테이지: S0 [base]  · 상태: 구현중 (코치가 gradle 뼈대 제공 완료 — 컴파일/테스트컴파일 green, 도메인 코드는 학습자 몫)
- 다음 행동: 학습자가 `market.*`에 도메인 코드(Controller/Entity/Repo/Service) 구현 → ProductApiSpec green 만들기. green 보고 오면 그릴링 없이 S1 도착 사건 투입.
- 현재 escalation: 없음 (S0은 base — compose는 mysql만 사용, 앱은 호스트 bootRun)

## 체크리스트
- [ ] S0 마켓 MVP (ProductApiSpec green)
- [ ] S1 [자원] 성장+트래픽 → 상세 p99
- [ ] S2 [정합성] 선착순 쿠폰 + --scale 3
- [ ] S3 [자원] 실시간 TOP10 랭킹
- [ ] S4 [장애] redis 다운 주입 + stampede

## 메모
- 스택: Java/Spring/MySQL  · 도메인: 이커머스 "요즘마켓"
- 컨테이너(고정): mysql:8(512m/1cpu 캡) + redis:7(vanilla) + [profile cluster] app·nginx(8088) + prometheus(9090) + grafana(3000). 앱 평시 호스트 bootRun(8080).
- (코치 리마인더) curriculum.md 내용·솔루션·다음 스테이지 누설 금지 — §1-2, §4
- (코치 리마인더) "불필요 졸업" 발동 시 여기 기록 + 남은 escalation 재설계 — §4
- (코치 리마인더) 시드·k6 스크립트는 S1 진입 시 코치가 완성 제공
