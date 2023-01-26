package com.axlebank.bankproductservice.model;

import com.axlebank.bankproductservice.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class BankProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    private String feature1;
    private String feature2;
    private String feature3;
}
