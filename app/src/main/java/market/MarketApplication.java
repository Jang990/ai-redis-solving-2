package market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 요즘마켓 앱 진입점. (배관 — 코치가 제공한 빈 뼈대)
 *
 * 네가 만들 도메인 코드(Controller / Entity / Repository / Service 등)는
 * 이 클래스와 같은 패키지(market) 또는 그 하위 패키지(market.*)에 두면
 * 컴포넌트 스캔에 자동으로 잡힌다. ProductApiSpec을 green으로 만드는 게 목표.
 */
@SpringBootApplication
public class MarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }
}
