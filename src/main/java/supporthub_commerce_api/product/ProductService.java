package supporthub_commerce_api.product;
import supporthub_commerce_api.exception.*;
import supporthub_commerce_api.exception.NotFoundException;
import supporthub_commerce_api.product.dto.*;
import supporthub_commerce_api.product.productCategory.ProductCategory;
import supporthub_commerce_api.product.repository.ProductRepository;
import supporthub_commerce_api.user.repository.UserRepository;
import supporthub_commerce_api.user.User;
import supporthub_commerce_api.user.enumsUser.UserRole;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import org.springframework.data.domain.*;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request){

        User user = getAuthenticationAdminUser();

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setProductCategory(request.productCategory());
        product.setStockQuantity(request.stockQuantity());
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedBy(user);

        return toResponse(productRepository.save(product));
    }

    public Page<ProductResponse> listActiveProduct(Pageable pageable){

        Page<Product> products = productRepository.findByActiveTrue(pageable);

        return products.map(this::toResponse);
    }

    public ProductResponse getProductById(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return toResponse(product);
    }

    public Page<ProductResponse> getMyProducts(Pageable pageable){

        User user = getAuthenticationAdminUser();
        Page<Product> products = productRepository
                .findByCreatedByAndActiveTrue(user, pageable);

        return products.map(this::toResponse);

    }

    @Transactional
    public ProductResponse updateProduct(CreateProductRequest request, Long id){

      User user = getAuthenticationAdminUser();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setProductCategory(request.productCategory());
        product.setStockQuantity(request.stockQuantity());

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void DeleteProduct(Long id){
        User user = getAuthenticationAdminUser();

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product not found"));

        product.setActive(false);
    }

    public Page<ProductResponse> searchProductsByName(String name, Pageable pageable){
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name, pageable);

    return products.map(this::toResponse);
    }

    public Page<ProductResponse> searchProductsByCategory(ProductCategory category, Pageable pageable){
        Page<Product> products = productRepository.findByProductCategoryAndActiveTrue(category,pageable);

        return products.map(this::toResponse);
    }


    private User getAuthenticationUser(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return user;
    }

    private User getAuthenticationAdminUser(){
        User user = getAuthenticationUser();

        if (user.getRole() != UserRole.ADMIN){
            throw new ForbiddenException("Only admins can perform this action");
        }
        return user;
    }

    private ProductResponse toResponse(Product product){
        Long createById = product.getCreatedBy() != null
                ? product.getCreatedBy().getId() : null;

        String createdByName = product.getCreatedBy() != null
                ? product.getCreatedBy().getName() : null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice() ,
                product.getProductCategory(),
                product.getStockQuantity(),
                product.getActive(),
                product.getCreatedAt().format(formatter),
                product.getCreatedBy().getId(),
                product.getCreatedBy().getName()
        );
    }
}
