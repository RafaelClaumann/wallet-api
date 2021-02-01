package br.com.application.wallet.models;

import org.junit.jupiter.api.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ExpenseTest {

	@Test
	void allExpenseMethodsWellImplementedTest() {
		final Class<Expense> expenseClass = Expense.class;

		assertPojoMethodsFor(expenseClass).areWellImplemented();
	}

}
