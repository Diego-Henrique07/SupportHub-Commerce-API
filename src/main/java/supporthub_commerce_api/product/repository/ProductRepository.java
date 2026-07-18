package supporthub_commerce_api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supporthub_commerce_api.product.Product;
import supporthub_commerce_api.product.dto.SearchProductCategoryRequest;
import supporthub_commerce_api.product.productCategory.ProductCategory;
import supporthub_commerce_api.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCreatedByAndActiveTrue(User createBy, Pageable pageable );
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);
    Page<Product> findByProductCategoryAndActiveTrue(ProductCategory category, Pageable pageable);
}
