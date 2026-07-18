package supporthub_commerce_api.order.entity;

import jakarta.persistence.*;
import lombok.*;
import supporthub_commerce_api.order.orderStatus.OrderStatus;
import supporthub_commerce_api.user.User;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Getter
    @Setter
    private BigDecimal totalAmount;

    @Getter
    @Setter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}
