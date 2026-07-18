package supporthub_commerce_api.order.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderItemRequest (

        @NotBlank
        Long productId,

        @NotBlank
        Integer quantity
){}
