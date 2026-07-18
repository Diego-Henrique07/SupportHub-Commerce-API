package supporthub_commerce_api.order.dto;

import supporthub_commerce_api.order.orderStatus.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus orderStatus
){}
