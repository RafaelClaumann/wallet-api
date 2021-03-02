package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.handler.exceptions.DuplicateDocumentException;
import br.com.application.wallet.mocks.MockWallet;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.application.wallet.mocks.MockClient.mockSingleClient;
import static br.com.application.wallet.mocks.MockExpense.mockMixedOpenClosedExpensesList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class ClientServiceTest {

	@InjectMocks
	private ClientService clientService;

	@Mock
	private ClientRepository clientRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void returnClientByIdTest() {
		Client client = mockSingleClient(1L);
		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		Client foundClient = clientService.findClientById(1L);

		verify(clientRepository).findById(1L);
		assertThat(foundClient).isEqualTo(client);
	}

	@Test
	void shouldThrowExceptionWhenClientNotFoundTest() {
		given(clientRepository.findById(any(Long.class))).willReturn(Optional.empty());

		assertThrows(ClientNotFoundException.class, () -> clientService.findClientById(1L));
	}

	@Test
	void findAllClientsTest() {
		Client client1 = mockSingleClient(1L);
		Client client2 = mockSingleClient(2L);
		List<Client> expectedClients = Arrays.asList(client1, client2);
		given(clientRepository.findAll()).willReturn(expectedClients);

		List<Client> foundClients = clientService.findAllClients();

		final Long expectedId = expectedClients.get(1).getId();
		final Long foundId = foundClients.get(1).getId();

		verify(clientRepository).findAll();
		assertThat(expectedId).isEqualTo(foundId);
		assertThat(expectedClients).isEqualTo(foundClients);
	}

	@Test
	void deleteClientWithAnEmptyListOfWalletsTest() {
		Client client = mockSingleClient(1L);
		client.setWallets(Collections.emptyList());

		given(clientRepository.findById(any(Long.class))).willReturn(Optional.of(client));
		doNothing().when(clientRepository).deleteById(any(Long.class));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientWithNullListOfWalletsTest() {
		Client client = mockSingleClient(1L);
		client.setWallets(null);

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean result = clientService.deleteClient(1L);

		assertTrue(result);
	}

	@Test
	void deleteClientPassingNullValueAsExpenseListTest() {
		Client client = mockSingleClient(1L);
		client.getWallets().forEach(wallet -> wallet.setExpenses(null));

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientPassingAnEmptyListAsExpenseListTest() {
		Client client = mockSingleClient(1L);
		client.getWallets().forEach(wallet -> wallet.setExpenses(Collections.emptyList()));

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientWithWalletContainingOnlyClosedExpensesTest() {
		Wallet wallet = MockWallet.mockSingleWalletWithClosedExpenses(1L);
		Client client = Client.builder().id(1L).name("First Client").wallets(Collections.singletonList(wallet)).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void shouldThrowExceptionWhenDeleteClientWithOpenExpensesTest() {
		List<Expense> expenses = mockMixedOpenClosedExpensesList();
		Wallet wallet = MockWallet.mockSingleWallet(1L, expenses);
		Client client = Client.builder().id(1L).name("First Client").wallets(Collections.singletonList(wallet)).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		Exception exception = assertThrows(OpenedExpensesException.class, () -> clientService.deleteClient(1L));
		String expectedMessage = "Cliente com id {1} possui pendencias na carteira!";
		String exceptionMessage = exception.getMessage();

		assertThat(expectedMessage).isEqualTo(exceptionMessage);
	}

	@Test
	void changeClientPropertiesTest() {
		String cpf = "531.521.400-10";
		String modifiedCpf = "124.327.440-98";

		Client client = mockSingleClient(1L, cpf);
		Client modifiedClient = mockSingleClient(1L, modifiedCpf);

		given(clientRepository.save(any(Client.class))).willReturn(client, modifiedClient);

		Client savedClient = clientService.saveClient(client);
		Client changedClient = clientService.changeClient(modifiedClient);

		assertThat(client).isEqualTo(savedClient);
		assertThat(modifiedClient).isEqualTo(changedClient);
	}

	@Test
	void clientSaveTest() {
		Client client = mockSingleClient(1L, "531.521.400-10");

		given(clientRepository.save(any(Client.class))).willReturn(client);

		Client savedClient = clientService.saveClient(client);

		verify(clientRepository).save(client);
		assertThat(client).isEqualTo(savedClient);
	}

	@Test
	void shouldThrowExceptionWhenSaveClientsWithSameCpfTest() {
		Client client = mockSingleClient(1L, "531.521.400-10");

		given(clientRepository.save(client)).willReturn(client).willThrow(DataIntegrityViolationException.class);

		clientService.saveClient(client);
		DuplicateDocumentException exception = assertThrows(DuplicateDocumentException.class,
				() -> clientService.saveClient(client));

		String expectedMessage = "CPF { 531.521.400-10 } já cadastrado";
		String exceptionMessage = exception.getMessage();

		assertThat(expectedMessage).isEqualTo(exceptionMessage);
	}

	@Test
	void checkClientAttributesValidationOnChangeClientMethodTest() {
		Client clientWithoutCpf = mockSingleClient(1L);
		clientWithoutCpf.setCpf(null);
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutCpf));

		Client clientWithoutName = mockSingleClient(2L);
		clientWithoutName.setName(null);
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutName));

		Client clientWithoutTelephone = mockSingleClient(3L);
		clientWithoutTelephone.setTelephoneNumber(null);
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutTelephone));
	}

}
