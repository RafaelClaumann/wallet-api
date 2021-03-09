package br.com.application.wallet.models;

import org.junit.jupiter.api.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ExpenseEntityTest {

	@Test
	void allExpenseMethodsWellImplementedTest() {
		final Class<ExpenseEntity> expenseClass = ExpenseEntity.class;

		assertPojoMethodsFor(expenseClass).areWellImplemented();
	}

}
