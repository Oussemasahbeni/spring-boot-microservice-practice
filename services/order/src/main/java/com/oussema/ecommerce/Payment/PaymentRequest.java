package com.oussema.ecommerce.Payment;

import com.oussema.ecommerce.customer.CustomerResponse;
import com.oussema.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(

        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer

) {
}
