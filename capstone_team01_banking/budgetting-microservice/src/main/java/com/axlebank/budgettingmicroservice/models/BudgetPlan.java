package com.axlebank.budgettingmicroservice.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor

@Entity
public class BudgetPlan {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    private int clientId;

    private double fixedAmount;

    private LocalDate startingDate;
    private LocalDate endingDate;

    @Transient
    List<BudgetResult> budgetResultList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BudgetPlan that = (BudgetPlan) o;
        return planId != null && Objects.equals(planId, that.planId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
