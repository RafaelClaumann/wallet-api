package br.com.application.wallet.mocks;

import br.com.application.wallet.models.ExpenseEntity;
import br.com.application.wallet.models.WalletEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockWallet {

	public static WalletEntity mockSingleWallet(final Long id) {
		return WalletEntity.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(new ArrayList<>()).build();
	}

	public static WalletEntity mockSingleWallet(final Long id, List<ExpenseEntity> expenses) {
		return WalletEntity.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expenses).build();
	}

	public static WalletEntity mockSingleWalletWithoutExpenses(final Long id) {
		return WalletEntity.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(new ArrayList<>()).build();
	}

	public static WalletEntity mockSingleWalletWithExpenses(final Long id, final List<ExpenseEntity> expensesList) {
		return WalletEntity.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expensesList).build();
	}

	public static WalletEntity mockSingleWalletWithClosedExpenses(final Long id) {
		final List<ExpenseEntity> expenses = MockExpense.mockTwoClosedExpensesList();
		return WalletEntity.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expenses).build();
	}

	public static WalletEntity mockSingleWalletWithOpenedExpenses(final Long id) {
		final List<ExpenseEntity> expenses = MockExpense.mockTwoOpenedExpensesList();
		return WalletEntity.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expenses).build();
	}

}
