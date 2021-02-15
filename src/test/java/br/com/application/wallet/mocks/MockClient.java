package br.com.application.wallet.mocks;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;

import java.util.Collections;
import java.util.List;

public class MockClient {
	
	public static Client mockSingleClientWithoutWalletsAndExpenses() {
		return Client.builder().id(1L).name("First Client").cpf("440.966.900-15").telephoneNumber("48 0 00000-0000")
				.wallets(Collections.emptyList()).build();
	}

	public static Client mockSingleClientWithWalletsAndExpenses() {
		List<Expense> expenses = MockExpense.mockTwoExpensesList();
		Wallet wallet = MockWallet.mockSingleWalletWithExpenses();

		return Client.builder().id(1L).name("First Client").cpf("440.966.900-15").telephoneNumber("48 0 00000-0000")
				.wallets(Collections.singletonList(wallet)).build();
	}
}
