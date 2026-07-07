package harness;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * S0 인수 테스트 — 상황·목표는 .learning/redis/brief.md 참조.
 * 계약 요약: 상품 등록/상세/가격수정, 조회수 누적(해당 조회 포함), 리뷰 개수·평균평점 반영, 없는 상품 404.
 * 전제: 앱이 BASE_URL(기본 http://localhost:8080)에서 기동 중, mysql은 docker compose up -d.
 * 이 테스트는 HTTP 행동만 본다 — 내부 구현은 검증하지 않는다.
 */
class ProductApiSpec {

    static final String BASE = System.getenv().getOrDefault("BASE_URL", "http://localhost:8080");
    static final HttpClient client = HttpClient.newHttpClient();
    static final ObjectMapper json = new ObjectMapper();

    @Test
    @DisplayName("상품을 등록하면 상세에서 등록한 그대로 조회된다")
    void 등록_후_상세_조회() throws Exception {
        long id = createProduct("무선 키보드", 39900, "전자기기");
        JsonNode d = getJson("/products/" + id, 200);
        assertEquals("무선 키보드", d.get("name").asText());
        assertEquals(39900, d.get("price").asInt());
        assertEquals("전자기기", d.get("category").asText());
    }

    @Test
    @DisplayName("상세 조회수는 조회할 때마다 누적된다 (해당 조회 포함)")
    void 조회수_누적() throws Exception {
        long id = createProduct("스텐 텀블러", 15900, "리빙");
        JsonNode last = null;
        for (int i = 0; i < 5; i++) {
            last = getJson("/products/" + id, 200);
        }
        assertEquals(5, last.get("viewCount").asInt(), "5번 조회했으면 마지막 응답의 viewCount는 5");
    }

    @Test
    @DisplayName("리뷰가 쌓이면 개수와 평균 평점이 상세에 반영된다")
    void 리뷰_요약_반영() throws Exception {
        long id = createProduct("캠핑 의자", 45000, "아웃도어");
        postJson("/products/" + id + "/reviews", "{\"rating\":4,\"content\":\"좋아요\"}");
        postJson("/products/" + id + "/reviews", "{\"rating\":5,\"content\":\"최고예요\"}");
        postJson("/products/" + id + "/reviews", "{\"rating\":3,\"content\":\"무난합니다\"}");
        JsonNode d = getJson("/products/" + id, 200);
        assertEquals(3, d.get("reviewCount").asInt());
        assertEquals(4.0, d.get("avgRating").asDouble(), 0.001);
    }

    @Test
    @DisplayName("가격을 수정하면 다음 조회에 즉시 반영된다")
    void 가격_수정_즉시_반영() throws Exception {
        long id = createProduct("에코백", 12000, "패션");
        getJson("/products/" + id, 200); // 수정 전에 이미 조회된 적 있는 상품이어야 한다
        HttpResponse<String> r = send(req("/products/" + id)
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"price\":9900}"))
                .header("Content-Type", "application/json")
                .build());
        assertTrue(r.statusCode() / 100 == 2, "PATCH 실패: " + r.statusCode() + " " + r.body());
        JsonNode d = getJson("/products/" + id, 200);
        assertEquals(9900, d.get("price").asInt(), "수정된 가격이 다음 조회에 바로 보여야 한다");
    }

    @Test
    @DisplayName("없는 상품 조회는 404")
    void 없는_상품_404() throws Exception {
        HttpResponse<String> r = send(req("/products/9999999999").GET().build());
        assertEquals(404, r.statusCode());
    }

    // ---------- helpers ----------

    long createProduct(String name, int price, String category) throws Exception {
        HttpResponse<String> r = postJson("/products",
                "{\"name\":\"" + name + "\",\"price\":" + price + ",\"category\":\"" + category + "\"}");
        return json.readTree(r.body()).get("id").asLong();
    }

    HttpResponse<String> postJson(String path, String body) throws Exception {
        HttpResponse<String> r = send(req(path)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build());
        assertTrue(r.statusCode() / 100 == 2, "POST " + path + " 실패: " + r.statusCode() + " " + r.body());
        return r;
    }

    JsonNode getJson(String path, int expectedStatus) throws Exception {
        HttpResponse<String> r = send(req(path).GET().build());
        assertEquals(expectedStatus, r.statusCode(), "GET " + path + " → " + r.body());
        return json.readTree(r.body());
    }

    HttpRequest.Builder req(String path) {
        return HttpRequest.newBuilder(URI.create(BASE + path));
    }

    HttpResponse<String> send(HttpRequest r) throws Exception {
        return client.send(r, HttpResponse.BodyHandlers.ofString());
    }
}
