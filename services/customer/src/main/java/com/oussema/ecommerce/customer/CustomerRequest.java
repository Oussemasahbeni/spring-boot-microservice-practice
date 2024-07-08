package com.oussema.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

public record CustomerRequest(

        String id,
        @NotNull(message = "First name is required")
        String firstName,
        @NotNull(message = "Last name is required")
        String lastName,
        @NotNull(message = "email is required")
        @Email(message = "email is invalid")
        String email,
        Address address
) {
}
