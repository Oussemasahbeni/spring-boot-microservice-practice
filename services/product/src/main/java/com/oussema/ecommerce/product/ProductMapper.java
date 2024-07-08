package com.oussema.ecommerce.product;

import com.oussema.ecommerce.category.Category;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
       return Product
               .builder()
               .id(request.id())
               .name(request.name())
               .price(request.price())
               .availableQuantity(request.availableQuantity())
               .category(
                       Category
                               .builder()
                               .id(request.categoryId())
                               .build())
               .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableQuantity(),
                product.getCategory().getId(),
                product.getCategory().getDescription()
        );
    }

    public ProductPurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
