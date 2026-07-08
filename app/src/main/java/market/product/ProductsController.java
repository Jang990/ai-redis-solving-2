package market.product;

import lombok.RequiredArgsConstructor;
import market.product.dto.ProductCreationRequest;
import market.product.dto.ProductCreationResponse;
import market.product.dto.ProductDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsService productsService;

    @PostMapping
    public ResponseEntity<ProductCreationResponse> createProduct(
            @RequestBody ProductCreationRequest productCreationRequest) {
        return ResponseEntity.ok(
                new ProductCreationResponse(
                        productsService.createProduct(productCreationRequest))
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> findProduct(@PathVariable long productId) {
        try {
            return ResponseEntity.ok(
                    toResponse(productsService.findProduct(productId))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> changeProduct(
            @PathVariable long productId,
            @RequestBody ProductCreationRequest productCreationRequest) {
        try {
            return ResponseEntity.ok(
                    toResponse(productsService.changeProduct(productId, productCreationRequest))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private static ProductDetailResponse toResponse(Products products) {
        return new ProductDetailResponse(
                products.getId(), products.getName(),
                products.getPrice(), products.getCategory(),
                products.getViewCount());
    }
}
