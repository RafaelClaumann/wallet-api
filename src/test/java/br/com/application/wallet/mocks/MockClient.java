package br.com.application.wallet.mocks;

import br.com.application.wallet.models.ClientEntity;
import br.com.application.wallet.models.ExpenseEntity;
import br.com.application.wallet.models.WalletEntity;

import java.util.Collections;
import java.util.List;

public class MockClient {

	public static ClientEntity mockSingleClient(final Long id) {
		List<ExpenseEntity> expenses = MockExpense.mockTwoClosedExpensesList();
		WalletEntity wallet = MockWallet.mockSingleWalletWithClosedExpenses(1L);
		return ClientEntity.builder().id(id).name("First Client").cpf("440.966.900-15").telephoneNumber("48 0 00000-0000")
				.wallets(Collections.singletonList(wallet)).build();
	}

	public static ClientEntity mockSingleClient(final Long id, final String cpf) {
		List<ExpenseEntity> expenses = MockExpense.mockTwoClosedExpensesList();
		WalletEntity wallet = MockWallet.mockSingleWalletWithClosedExpenses(1L);
		return ClientEntity.builder().id(id).name("First Client").cpf(cpf).telephoneNumber("48 0 00000-0000")
				.wallets(Collections.singletonList(wallet)).build();
	}

	public static ClientEntity mockSingleClient(final Long id, final List<WalletEntity> wallets) {
		List<ExpenseEntity> expenses = MockExpense.mockTwoClosedExpensesList();
		return ClientEntity.builder().id(id).name("First Client").cpf("440.966.900-15").telephoneNumber("48 0 00000-0000")
				.wallets(wallets).build();
	}

}
