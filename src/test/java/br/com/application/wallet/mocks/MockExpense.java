package br.com.application.wallet.mocks;

import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.models.enums.ExpenseType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class MockExpense {

	public static Expense mockSingleExpense() {
		return Expense.builder().id(1L).description("despesa1").value(BigDecimal.ONE).expenseType(ExpenseType.OTHER)
				.expenseState(ExpenseState.CLOSED).build();
	}

	public static List<Expense> mockTwoExpensesList() {
		Expense expense1 = Expense.builder().id(1L).description("despesa1").value(BigDecimal.ONE)
				.expenseType(ExpenseType.OTHER).expenseState(ExpenseState.CLOSED).build();
		Expense expense2 = Expense.builder().id(2L).description("despesa2").value(BigDecimal.TEN)
				.expenseType(ExpenseType.CAR).expenseState(ExpenseState.CLOSED).build();
		return Arrays.asList(expense1, expense2);
	}
}
