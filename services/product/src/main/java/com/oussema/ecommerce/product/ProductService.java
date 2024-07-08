package com.oussema.ecommerce.product;

import com.oussema.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest request) {
        var product = productMapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
       var productIds = request.stream()
               .map(ProductPurchaseRequest::productId)
               .toList();
       var storedProducts = productRepository.findAllByIdInOrderById(productIds);
       if(productIds.size() != storedProducts.size()) {
           throw new ProductPurchaseException("One or more products not found");
       }
       var storedRequest= request
               .stream()
               .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
               .toList();
       var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

       for (int i= 0 ; i < storedProducts.size(); i++) {
           var product = storedProducts.get(i);
           var productRequest = storedRequest.get(i);
           if(product.getAvailableQuantity() < productRequest.quantity()) {
               throw new ProductPurchaseException("Not enough quantity for product with id " + product.getId());
           }
           var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
           product.setAvailableQuantity(newAvailableQuantity);
           productRepository.save(product);
           purchasedProducts.add(productMapper.toProductPurchaseResponse(product,productRequest.quantity()));

       }

       return purchasedProducts;

    }

    public ProductResponse findById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
