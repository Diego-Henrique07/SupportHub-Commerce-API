package supporthub_commerce_api.product.dto;

import supporthub_commerce_api.product.productCategory.ProductCategory;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        ProductCategory productCategory,
        Integer stockQuantity,
        Boolean active,
        String createdAt,
        Long createdById,
        String createdByName

){}
