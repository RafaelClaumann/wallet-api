package br.com.application.wallet.mocks;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;

import java.util.Collections;
import java.util.List;

public class MockClient {

	public static Client mockSingleClient(final Long id) {
		List<Expense> expenses = MockExpense.mockTwoClosedExpensesList();
		Wallet wallet = MockWallet.mockSingleWalletWithClosedExpenses(1L);
		return Client.builder().id(id).name("First Client").cpf("440.966.900-15").telephoneNumber("48 0 00000-0000")
				.wallets(Collections.singletonList(wallet)).build();
	}

	public static Client mockSingleClient(final Long id, final String cpf) {
		List<Expense> expenses = MockExpense.mockTwoClosedExpensesList();
		Wallet wallet = MockWallet.mockSingleWalletWithClosedExpenses(1L);
		return Client.builder().id(id).name("First Client").cpf(cpf).telephoneNumber("48 0 00000-0000")
				.wallets(Collections.singletonList(wallet)).build();
	}

	public static Client mockSingleClient(final Long id, final List<Wallet> wallets) {
		List<Expense> expenses = MockExpense.mockTwoClosedExpensesList();
		return Client.builder().id(id).name("First Client").cpf("440.966.900-15").telephoneNumber("48 0 00000-0000")
				.wallets(wallets).build();
	}

}
