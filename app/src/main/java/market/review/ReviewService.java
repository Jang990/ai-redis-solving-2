package market.review;

import lombok.RequiredArgsConstructor;
import market.product.Products;
import market.product.ProductsRepository;
import market.review.dto.ReviewCreationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ProductsRepository productsRepository;
    private final ReviewRepository reviewRepository;

    public Reviews createReview(
            long productId,
            ReviewCreationRequest request) {
        Products products = productsRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);

        Reviews reviews = new Reviews(request.rating(), request.content());
        products.addReview(reviews.getRating());

        return reviewRepository.save(reviews);
    }
}
