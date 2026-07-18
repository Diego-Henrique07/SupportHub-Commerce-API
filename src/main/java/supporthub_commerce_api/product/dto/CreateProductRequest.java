package supporthub_commerce_api.product.dto;
import supporthub_commerce_api.product.productCategory.ProductCategory;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        @Positive
        BigDecimal price,

        ProductCategory productCategory,

        @NotNull
        @PositiveOrZero
        Integer stockQuantity
){}
