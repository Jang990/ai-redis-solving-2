package market.product;

import lombok.RequiredArgsConstructor;
import market.product.dto.ProductCreationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;

    public long createProduct(
            ProductCreationRequest productCreationRequest) {
        Products result = productsRepository.save(
                new Products(
                        productCreationRequest.name(),
                        productCreationRequest.price(),
                        productCreationRequest.category())
        );

        return result.getId();
    }

    public Products findProduct(long productId) {
        Products products = productsRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
        products.increaseViewCount();
        return products;
    }

    public Products changeProduct(
            long productId,
            ProductCreationRequest productCreationRequest) {
        Products products = productsRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);

        products.changeAll(
                productCreationRequest.name(),
                productCreationRequest.price(),
                productCreationRequest.category());

        return products;
    }
}
