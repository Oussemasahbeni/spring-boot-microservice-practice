package com.oussema.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(

        Integer id,
        @NotNull(message = " Product Name is required")
        String name,
        @NotNull(message = " Product Description is required")
        String description,
        @NotNull(message = " Product Price is required")
        @Positive(message = " Product Price must be greater than 0")
        BigDecimal price,
        @NotNull(message = " Product Available Quantity is required")
        @Positive(message = " Product Available Quantity must be greater than 0")
        double availableQuantity,
        @NotNull(message = " Product Category Id is required")
        Integer categoryId
        ) {
}
