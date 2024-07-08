package com.oussema.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
