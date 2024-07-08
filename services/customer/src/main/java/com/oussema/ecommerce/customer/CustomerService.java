package com.oussema.ecommerce.customer;

import ch.qos.logback.core.util.StringUtil;
import com.oussema.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.id()).orElseThrow(
                () -> new CustomerNotFoundException (format(
                        "Cannot Update customer :: No customer found with id %s",
                        request.id()
                ))
        );
        mergeCustomer(customer, request);
        repository.save(customer);

    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName()))
            customer.setFirstName(request.firstName());
        if(StringUtils.isNotBlank(request.lastName()))
            customer.setLastName(request.lastName());
        if(StringUtils.isNotBlank(request.email()))
            customer.setEmail(request.email());
        if(request.address() != null)
            customer.setAddress(request.address());
    }

    public List<CustomerResponse> getAll() {

        return repository.findAll().stream()
                .map(mapper::toCustomerResponse)
                .toList();
    }

    public CustomerResponse findById(String id) {
        return  repository.findById(id)
                .map(mapper::toCustomerResponse)
                .orElseThrow(
                () -> new CustomerNotFoundException(format(
                        "No customer found with id %s",
                        id
                ))
        );
    }

    public Boolean existsById(String id) {
        return repository.findById(id).isPresent();
    }

    public void deleteById(String id) {
        if(!existsById(id))
            throw new CustomerNotFoundException(format(
                    "Cannot delete customer :: No customer found with id %s",
                    id
            ));
        repository.deleteById(id);
    }
}
