package com.oussema.ecommerce.kafka;

import com.oussema.ecommerce.customer.CustomerResponse;
import com.oussema.ecommerce.order.PaymentMethod;
import com.oussema.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
