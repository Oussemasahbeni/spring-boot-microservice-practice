package com.oussema.ecommerce.product;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(
        @NotNull(message = "productId is required")
        Integer productId,
        @NotNull(message = "quantity is required")
        double quantity
) {
}
