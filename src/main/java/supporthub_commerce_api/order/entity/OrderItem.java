package supporthub_commerce_api.order.entity;

import jakarta.persistence.*;
import lombok.*;
import supporthub_commerce_api.product.Product;
import java.math.BigDecimal;

@Entity
@Table(name = "Order_items")
public class OrderItem {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Integer quantity;

    @Getter
    @Setter
    private BigDecimal unitPrice;

    @Getter
    @Setter
    private BigDecimal subtotal;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "Order_id")
    private Order order;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
