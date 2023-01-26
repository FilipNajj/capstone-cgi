package com.axlebank.budgettingmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories_limit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetResult {

    @Id
    private String category;

    private double percentage;
}
