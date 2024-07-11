package com.oussema.ecommerce.product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,

        String name,

        String description,

        BigDecimal price,

        double availableQuantity,

        Integer categoryId,
        String categoryDescription
) {
}
