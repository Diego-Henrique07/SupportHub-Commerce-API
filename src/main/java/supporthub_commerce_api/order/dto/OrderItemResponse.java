package supporthub_commerce_api.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String product,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal

){}
