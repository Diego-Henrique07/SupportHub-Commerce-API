package supporthub_commerce_api.product.dto;
import supporthub_commerce_api.product.productCategory.ProductCategory;

public record SearchProductCategoryRequest(
        ProductCategory category
){}
