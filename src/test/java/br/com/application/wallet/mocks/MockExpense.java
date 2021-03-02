package br.com.application.wallet.mocks;

import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.models.enums.ExpenseType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockExpense {

	public static Expense mockSingleClosedExpense(final Long id) {
		return Expense.builder().id(id).description("despesa1").value(BigDecimal.ONE).expenseType(ExpenseType.OTHER)
				.expenseState(ExpenseState.CLOSED).build();
	}

	public static Expense mockSingleOpenedExpense(final Long id) {
		return Expense.builder().id(id).description("despesa1").value(BigDecimal.ONE).expenseType(ExpenseType.OTHER)
				.expenseState(ExpenseState.OPEN).build();
	}

	public static List<Expense> mockTwoOpenedExpensesList() {
		return  generateListOfExpenses(true, false, false);
	}

	public static List<Expense> mockTwoClosedExpensesList() {
		return generateListOfExpenses(false, true, false);
	}

	public static List<Expense> mockMixedOpenClosedExpensesList() {
		return generateListOfExpenses(false, false, true);
	}

	private static List<Expense> generateListOfExpenses(final Boolean onlyOpened, final Boolean onlyClosed,
			final Boolean mixed) {
		Expense expense1 = mockSingleClosedExpense(1L);
		Expense expense2 = mockSingleClosedExpense(2L);
		List<Expense> expenses = Arrays.asList(expense1, expense2);

		if (onlyOpened) {
			expenses.get(0).setExpenseState(ExpenseState.OPEN);
			expenses.get(1).setExpenseState(ExpenseState.OPEN);
			return expenses;
		}

		if (mixed) {
			expenses.get(0).setExpenseState(ExpenseState.OPEN);
			return expenses;
		}

		if(onlyClosed) {
			return  expenses;
		}

		return new ArrayList<>();
	}

}
