package com.oussema.ecommerce.email;

import lombok.Getter;

public enum EmailTemplate {

    PAYMENT_CONFIRMATION("payment.confirmation.html", "Payment Successfully processed"),
    ORDER_CONFORMATION("order.confirmation.html", "Order Successfully processed");

    @Getter
    private final String template;

    @Getter
    private final String subject;


    EmailTemplate(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
