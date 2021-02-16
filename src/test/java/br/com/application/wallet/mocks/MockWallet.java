package br.com.application.wallet.mocks;

import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockWallet {

	public static Wallet mockSingleWallet(final Long id) {
		return Wallet.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(new ArrayList<>()).build();
	}

	public static Wallet mockSingleWallet(final Long id, List<Expense> expenses) {
		return Wallet.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expenses).build();
	}

	public static Wallet mockSingleWalletWithoutExpenses(final Long id) {
		return Wallet.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(new ArrayList<>()).build();
	}

	public static Wallet mockSingleWalletWithClosedExpenses(final Long id) {
		final List<Expense> expenses = MockExpense.mockTwoClosedExpensesList();
		return Wallet.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expenses).build();
	}

	public static Wallet mockSingleWalletWithOpenedExpenses(final Long id) {
		final List<Expense> expenses = MockExpense.mockTwoOpenedExpensesList();
		return Wallet.builder().id(id).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expenses).build();
	}

}
