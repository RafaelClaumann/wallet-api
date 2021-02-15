package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.Expense;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExpenseDTO {

	@JsonProperty("expense_id")
	private final Long id;

	@JsonProperty("expense_description")
	private final String description;

	@JsonProperty("expense_value")
	private final BigDecimal value;

	@JsonProperty("expense_state")
	private final String expenseState;

	@JsonProperty("expense_type")
	private final String expenseType;

	public ExpenseDTO(Expense expense) {
		this.id = expense.getId();
		this.description = expense.getDescription();
		this.value = expense.getValue();
		this.expenseType = expense.getExpenseType().name();
		this.expenseState = expense.getExpenseState().name();
	}
}
