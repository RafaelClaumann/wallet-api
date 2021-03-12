package br.com.application.wallet.models.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpenseForm {

    @JsonProperty("expense_description")
    private String description;

    @JsonProperty("expense_value")
    private BigDecimal value;

    @JsonProperty("expense_type_id")
    private Integer expenseTypeId;

    @JsonProperty("expense_state_id")
    private Integer expenseStateId;

}
