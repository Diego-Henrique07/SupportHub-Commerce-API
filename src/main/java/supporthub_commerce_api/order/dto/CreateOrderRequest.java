package supporthub_commerce_api.order.dto;

import java.util.List;

public record CreateOrderRequest(

        List<OrderItemRequest> items

){}
