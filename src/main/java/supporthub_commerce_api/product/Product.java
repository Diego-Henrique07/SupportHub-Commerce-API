package supporthub_commerce_api.product;

import jakarta.persistence.*;
import lombok.*;
import supporthub_commerce_api.user.User;
import supporthub_commerce_api.product.productCategory.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private BigDecimal price;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Getter
    @Setter
    private Integer stockQuantity;

    @Getter
    @Setter
    private Boolean active;

    @Getter
    @Setter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;


}
