package supporthub_commerce_api.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supporthub_commerce_api.order.entity.Order;
import supporthub_commerce_api.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUser(User user, Pageable pageable);

}
