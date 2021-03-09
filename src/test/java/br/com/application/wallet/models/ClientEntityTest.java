package br.com.application.wallet.models;

import org.junit.jupiter.api.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ClientEntityTest {

	@Test
	void allClientMethodsWellImplementedTest() {
		final Class<ClientEntity> clientClass = ClientEntity.class;

		assertPojoMethodsFor(clientClass).areWellImplemented();
	}

}
