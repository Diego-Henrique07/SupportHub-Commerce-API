package supporthub_commerce_api.order;
import supporthub_commerce_api.order.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createProduct( @Valid @RequestBody CreateOrderRequest orderRequest){

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(Pageable pageable){
        return ResponseEntity.ok(orderService.getMyOrders(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@Valid @RequestBody UpdateOrderStatusRequest request, @PathVariable Long id){

        return ResponseEntity.ok(orderService.updateOrder(request, id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }
}
