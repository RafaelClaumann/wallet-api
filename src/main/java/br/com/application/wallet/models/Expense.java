package br.com.application.wallet.models;

import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.models.enums.ExpenseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EXPENSE")
public class Expense implements Serializable {
    private static final long serialVersionUID = 2717526289026820456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private ExpenseState expenseState;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    public Expense(String description, BigDecimal value) {
        this(description, value, null);
    }

    public Expense(String description, BigDecimal value, ExpenseState expenseState) {
        this(description, value, expenseState, null);
    }

    public Expense(String description, BigDecimal value, ExpenseState expenseState, ExpenseType expenseType) {
        this.description = description;
        this.value = value;
        this.expenseState = expenseState;
        this.expenseType = expenseType;
    }
}
