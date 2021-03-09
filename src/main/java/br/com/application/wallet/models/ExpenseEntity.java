package br.com.application.wallet.models;

import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.models.enums.ExpenseType;
import lombok.*;

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
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EXPENSE")
public class ExpenseEntity implements Serializable {
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

    public ExpenseEntity(String description, BigDecimal value) {
        this(description, value, null);
    }

    public ExpenseEntity(String description, BigDecimal value, ExpenseState expenseState) {
        this(description, value, expenseState, null);
    }

    public ExpenseEntity(String description, BigDecimal value, ExpenseState expenseState, ExpenseType expenseType) {
        this.description = description;
        this.value = value;
        this.expenseState = expenseState;
        this.expenseType = expenseType;
    }
}
