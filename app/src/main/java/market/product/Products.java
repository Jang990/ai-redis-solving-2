package market.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    private String category;
    private int viewCount;

    private int reviewCount;
    private double ratingSum;

    public Products(String name, int price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
        viewCount = 0;
        reviewCount = 0;
        ratingSum = 0;
    }

    public void changeAll(String name, int price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void addReview(double rating) {
        ratingSum += rating;
        reviewCount++;
    }

    public double getAvgRating() {
        return ratingSum / reviewCount;
    }
}
