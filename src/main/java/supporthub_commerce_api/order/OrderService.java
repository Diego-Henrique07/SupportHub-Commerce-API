package supporthub_commerce_api.order;
import supporthub_commerce_api.order.orderStatus.OrderStatus;
import supporthub_commerce_api.order.repository.OrderRepository;
import supporthub_commerce_api.product.repository.ProductRepository;
import supporthub_commerce_api.user.User;
import supporthub_commerce_api.user.repository.UserRepository;
import supporthub_commerce_api.user.enumsUser.UserRole;
import supporthub_commerce_api.exception.BadRequestException;
import supporthub_commerce_api.exception.ForbiddenException;
import supporthub_commerce_api.exception.NotFoundException;
import supporthub_commerce_api.order.entity.Order;
import supporthub_commerce_api.order.entity.OrderItem;
import supporthub_commerce_api.product.Product;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import supporthub_commerce_api.order.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.*;



@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){

        User user = getAuthenticationUser();

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setCreatedAt(LocalDateTime.now());

        for (OrderItemRequest itemRequest : request.items()){

            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new NotFoundException("Product not found")
                    );

            if (Boolean.FALSE.equals(product.getActive())){
                throw new BadRequestException("Product is not active");
            }

            if (itemRequest.quantity() <= 0){
                throw new BadRequestException("Insufficient stock");
            }

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.quantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubtotal(subtotal);

            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.quantity()
            );

            order.getItems().add(orderItem);
            order.setTotalAmount(order.getTotalAmount().add(subtotal));
        }

        Order saved = orderRepository.save(order);

        List<OrderItemResponse> items = saved.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .toList();

        return new OrderResponse(
                saved.getId(),
                saved.getOrderStatus().name(),
                saved.getTotalAmount(),
                saved.getCreatedAt().format(formatter),
                items
        );
    }

    public Page<OrderResponse> getMyOrders(Pageable pageable){
        User user = getAuthenticationUser();

        Page<Order> orders = orderRepository.findByUser(user, pageable);

        return orders.map(this::toResponse);
    }

    public OrderResponse getOrderById(Long id){

        User authenticationUser = getAuthenticationUser();

        Order order =  orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("order not found"));

        if (!order.getUser().getId().equals(authenticationUser.getId())){
            throw new ForbiddenException("You are not allowed to view this order");
        }

        return toResponse(order);
    }

    public OrderResponse updateOrder(UpdateOrderStatusRequest request, Long id){

        User user = getAuthenticationAdminUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found")
                );

        if (!order.getUser().getId().equals(user.getId())){
            throw new ForbiddenException("You can only access your own orders.");
        }

        if (user.getRole() != UserRole.ADMIN){
            throw new ForbiddenException("Only administrators can perform this action.");
        }

        orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Order not found"));

        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus newStatus = request.orderStatus();

        boolean isValid =
                (currentStatus == OrderStatus.CREATED && newStatus == OrderStatus.PAID) ||
                (currentStatus == OrderStatus.CREATED && newStatus == OrderStatus.CANCELED) ||
                (currentStatus == OrderStatus.PAID && newStatus == OrderStatus.PROCESSING) ||
                (currentStatus == OrderStatus.PAID && newStatus == OrderStatus.CANCELED) ||
                (currentStatus == OrderStatus.PROCESSING && newStatus == OrderStatus.SHIPPED) ||
                (currentStatus == OrderStatus.SHIPPED && newStatus == OrderStatus.DELIVERED);

        if (!isValid){
            throw new BadRequestException("Invalid status transition");
        }

        order.setOrderStatus(newStatus);
        return toResponse(orderRepository.save(order));
    }

    public OrderResponse cancelOrder(Long id ){
        User authenticationUser = getAuthenticationUser();

        Order order =  orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Order not found"));

        if (!order.getUser().getId().equals(authenticationUser.getId())){
            throw new ForbiddenException("You are not allowed to view this order");
        }



        if (order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.PAID){
            throw new BadRequestException(
                    "Only orders in CREATED  or PAID status can be canceled"
            );
        }

        order.setOrderStatus(OrderStatus.CANCELED);

        return toResponse(orderRepository.save(order));
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

    private OrderResponse toResponse(Order order) {

        String createdAt = order.getCreatedAt() == null ? null : order.getCreatedAt().format(formatter);

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderStatus().name(),
                order.getTotalAmount(),
                createdAt,
                items
        );
    }
}
