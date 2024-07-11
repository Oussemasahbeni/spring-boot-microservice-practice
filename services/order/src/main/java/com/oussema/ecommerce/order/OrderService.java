package com.oussema.ecommerce.order;

import com.oussema.ecommerce.Payment.PaymentClient;
import com.oussema.ecommerce.Payment.PaymentRequest;
import com.oussema.ecommerce.customer.CustomerClient;
import com.oussema.ecommerce.exception.BusinessException;
import com.oussema.ecommerce.kafka.OrderConfirmation;
import com.oussema.ecommerce.kafka.OrderProducer;
import com.oussema.ecommerce.orderline.OrderLineRequest;
import com.oussema.ecommerce.orderline.OrderLineService;
import com.oussema.ecommerce.product.ProductClient;
import com.oussema.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;


    public Integer create(OrderRequest request) {
        // check the customer -> open feign
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(
                        () -> new BusinessException("Cannot create order, no customer found with id " + request.customerId())
                );

        // purchase the product --> using product micro-service (RestTemplate)

       var purchasedProducts= productClient.purchaseProduct(request.products());

        // persist the order
        var order= this.orderRepository.save(mapper.toOrder(request));

        // persist the order lines
        for(PurchaseRequest purchaseRequest: request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        // start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);


        // send the order confirmation to our notification service ( kafka )
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(mapper::fromOrder)
                .toList();
    }

    public OrderResponse findById(Integer id) {
        return orderRepository.findById(id)
                .map(mapper::fromOrder)
                .orElseThrow(
                        () -> new EntityNotFoundException(format("No order found with id %d", id))
                );
    }
}
