package market.review;

import lombok.RequiredArgsConstructor;
import market.review.dto.ReviewCreationRequest;
import market.review.dto.ReviewCreationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewCreationResponse> createReview(
            @PathVariable long productId,
            @RequestBody ReviewCreationRequest reviewCreationRequest) {
        Reviews review = reviewService.createReview(productId, reviewCreationRequest);
        return ResponseEntity.ok(
                new ReviewCreationResponse(
                        review.getId(),
                        review.getRating(),
                        review.getContent())
        );
    }
}
