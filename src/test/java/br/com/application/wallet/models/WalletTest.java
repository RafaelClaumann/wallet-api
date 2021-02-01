package br.com.application.wallet.models;

import org.junit.jupiter.api.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class WalletTest {

	@Test
	void allWalletMethodsWellImplementedTest() {
		final Class<Wallet> walletClass = Wallet.class;

		assertPojoMethodsFor(walletClass).areWellImplemented();
	}
}
