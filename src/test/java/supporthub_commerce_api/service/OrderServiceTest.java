package supporthub_commerce_api.service;
import supporthub_commerce_api.exception.*;
import supporthub_commerce_api.order.entity.Order;
import supporthub_commerce_api.order.OrderService;
import supporthub_commerce_api.order.dto.*;
import supporthub_commerce_api.order.orderStatus.OrderStatus;
import supporthub_commerce_api.order.repository.OrderRepository;
import supporthub_commerce_api.product.Product;
import supporthub_commerce_api.product.repository.ProductRepository;
import supporthub_commerce_api.user.User;
import supporthub_commerce_api.user.repository.UserRepository;
import supporthub_commerce_api.user.enumsUser.UserRole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;


    @Test
    void createOrder_ShouldThrowBadRequestException_WhenProductIsInactive(){
        Long productId = 1L;

        User user = new User();
        user.setEmail("user@email.com");
        user.setRole(UserRole.ADMIN);
        mockAuthenticatedUser(user);

        OrderItemRequest item = new OrderItemRequest(productId, 2);
        CreateOrderRequest request = new CreateOrderRequest(List.of(item));

        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setActive(false);
        product.setStockQuantity(10);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(request);
        });

        verify(productRepository).findById(productId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrowBadRequestException_whenTheQuantityIsLessThanOrEqualToZero(){
        Long productId = 1L;
        Order order = new Order();
        Product product = new Product();

        User user = new User();
        user.setEmail("user@email.com");
        mockAuthenticatedUser(user);

        OrderItemRequest itemRequest = new OrderItemRequest(productId, 0);
        CreateOrderRequest request = new CreateOrderRequest(List.of(itemRequest));

        product.setId(productId);
        product.setName("Test Product");
        product.setActive(true);
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStockQuantity(10);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(request);
                });

        verify(productRepository).findById(productId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrder_ShouldThrowForbiddenException_WhenUserDoesNotOwnTheOrder(){
        Long orderId = 1L;

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setEmail("user@email.com");

        mockAuthenticatedUser(loggedUser);

        User orderOwner = new User();
        orderOwner.setId(2L);
        orderOwner.setEmail("user@email.com");

        Order order = new Order();
        order.setId(orderId);
        order.setUser(orderOwner);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(ForbiddenException.class, () -> {
            orderService.getOrderById(orderId);
        });

        verify(orderRepository).findById(orderId);
    }

    @Test
    void updateOrderStatus_ShouldThrowBadRequestException_WhenStatusTransitionIsInvalid(){
        Long order1 = 1L;

        User admin= new User();
        admin.setId(order1);
        admin.setEmail("adminUser@gmail.com");
        admin.setRole(UserRole.ADMIN);
        mockAuthenticatedAdminUser(admin);

        Order orderAdmin = new Order();
        orderAdmin.setId(order1);
        orderAdmin.setUser(admin);
        orderAdmin.setOrderStatus(OrderStatus.CREATED);

        Order request = new Order();
        request.setId(order1);
        request.setUser(admin);
        request.setOrderStatus(OrderStatus.DELIVERED);

        UpdateOrderStatusRequest UpdateRequest = new UpdateOrderStatusRequest(request.getOrderStatus());

        when(orderRepository.findById(order1))
                .thenReturn(Optional.of(orderAdmin));

        assertThrows(BadRequestException.class, () -> {
            orderService.updateOrder(UpdateRequest, order1);
        });

    }

    @Test
    void cancelOrder_ShouldCancelOrder_WhenStatusIsCreated(){
        Long orderId = 1L;

        User user = new User();
        user.setId(orderId);
        user.setEmail("user@email.com");
        mockAuthenticatedUser(user);

        Order orderCreate = new Order();
        orderCreate.setId(orderId);
        orderCreate.setUser(user);
        orderCreate.setOrderStatus(OrderStatus.CREATED);

        Order request = new Order();
        request.setId(orderId);
        request.setUser(user);
        request.setOrderStatus(OrderStatus.CANCELED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(request));

        assertThrows(BadRequestException.class, () -> {
            orderService.cancelOrder(orderId);
        });
    }

    @Test
    void cancelOrder_ShouldCancelOrder_WhenStatusIsPaid(){
        Long orderId = 1L;

        User user = new User();
        user.setId(orderId);
        user.setEmail("user@email.com");
        mockAuthenticatedUser(user);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PAID);

        Order request = new Order();
        request.setId(orderId);
        request.setUser(user);
        request.setOrderStatus(OrderStatus.CANCELED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(request));

        assertThrows(BadRequestException.class, () ->{
                    orderService.cancelOrder(orderId);
        });
    }

    @Test
    void cancelOrder_ShouldThrowBadRequestException_WhenOrderCannotBeCanceled(){
        Long orderId = 1L;

        User user = new User();
        user.setId(orderId);
        user.setEmail("user@email.com");
        mockAuthenticatedUser(user);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CANCELED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () ->{
            orderService.cancelOrder(orderId);
        });

        verify(orderRepository).findById(orderId);
        verify(orderRepository,never()).save(any(Order.class));
    }

    @Test
    void cancelOrder_ShouldThrowNotFoundException_WhenOrderDoesNotExist(){
        Long orderId = 1L;

        User user = new User();
        user.setEmail("user@email.com");
        mockAuthenticatedUser(user);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            orderService.cancelOrder(orderId);
        });

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        Long productId = 1L;

        User user = new User();

        user.setEmail("user@email.com");
        user.setRole(UserRole.ADMIN);
        mockAuthenticatedAdminUser(user);

        OrderItemRequest item = new OrderItemRequest(productId, 2);
        CreateOrderRequest request = new CreateOrderRequest(List.of(item));

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            orderService.createOrder(request);
        });

        verify(productRepository).findById(productId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    private void mockAuthenticatedUser(User user){

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("user@email.com");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByEmail("user@email.com"))
                .thenReturn(Optional.of(user));
    }

    private void mockAuthenticatedAdminUser(User admin){

        Authentication authentication = mock(Authentication.class);

        admin.setRole(UserRole.ADMIN);
        when(authentication.getName()).thenReturn("adminUser@gmail.com");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByEmail("adminUser@gmail.com"))
                .thenReturn(Optional.of(admin));
    }
}