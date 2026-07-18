package supporthub_commerce_api.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supporthub_commerce_api.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
