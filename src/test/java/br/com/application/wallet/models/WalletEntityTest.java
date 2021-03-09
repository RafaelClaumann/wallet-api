package br.com.application.wallet.models;

import org.junit.jupiter.api.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class WalletEntityTest {

	@Test
	void allWalletMethodsWellImplementedTest() {
		final Class<WalletEntity> walletClass = WalletEntity.class;

		assertPojoMethodsFor(walletClass).areWellImplemented();
	}
}
