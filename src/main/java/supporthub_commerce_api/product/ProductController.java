package supporthub_commerce_api.product;
import supporthub_commerce_api.product.productCategory.ProductCategory;
import supporthub_commerce_api.product.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Transactional
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest productRequest) {

        ProductResponse response = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.listActiveProduct(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/my-products")
    public ResponseEntity<Page<ProductResponse>> getMyProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getMyProducts(pageable));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProducts(
            @Valid @RequestBody CreateProductRequest request,
            @PathVariable Long id) {

        return ResponseEntity.ok(productService.updateProduct(request, id));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        productService.DeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProductsByName(
            @RequestParam String name,
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.searchProductsByName(name, pageable));
    }

    @GetMapping("/category")
    public ResponseEntity<Page<ProductResponse>> searchProductCategory(
            @RequestParam ProductCategory category,
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.searchProductsByCategory(category, pageable));

    }



}
