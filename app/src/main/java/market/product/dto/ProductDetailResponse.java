package market.product.dto;

public record ProductDetailResponse(
        long id, String name,
        int price, String category,
        int viewCount, int reviewCount, double avgRating) {
}
