package br.com.application.wallet.models;

import org.junit.jupiter.api.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ClientTest {

	@Test
	void allClientMethodsWellImplementedTest() {
		final Class<Client> clientClass = Client.class;

		assertPojoMethodsFor(clientClass).areWellImplemented();
	}

}
