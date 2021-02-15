package br.com.application.wallet.mocks;

import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockWallet {

	public static Wallet mockSingleWalletWithoutExpenses() {
		return Wallet.builder().id(1L).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(new ArrayList<>()).build();
	}

	public static Wallet mockSingleWalletWithExpenses() {
		final List<Expense> expenses = MockExpense.mockTwoExpensesList();
		return Wallet.builder().id(1L).description("carteira principal").balance(BigDecimal.valueOf(8000))
				.expenses(expenses).build();
	}
}
