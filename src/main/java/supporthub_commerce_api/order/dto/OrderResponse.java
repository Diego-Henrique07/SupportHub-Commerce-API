package supporthub_commerce_api.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        BigDecimal totalAmount,
        String createdAt,
        List<OrderItemResponse> items
){}
