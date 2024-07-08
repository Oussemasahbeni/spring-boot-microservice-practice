package com.oussema.ecommerce.product;

import com.oussema.ecommerce.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private double availableQuantity;
    private BigDecimal price; // Use big decimal for money
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;



}
