package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.ClientOpenedExpensesException;
import br.com.application.wallet.handler.exceptions.DuplicateDocumentException;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
		long clientId = 1L;
		Client client = Client.builder().id(clientId).name("Rafael Claumann").build();
		given(clientRepository.findById(clientId)).willReturn(Optional.of(client));

		Client foundClient = clientService.findClientById(clientId);

		verify(clientRepository).findById(clientId);
		assertThat(foundClient).isEqualTo(client);
	}

	@Test
	void shouldThrowExceptionWhenClientNotFoundTest() {
		given(clientRepository.findById(any(Long.class))).willReturn(Optional.empty());

		assertThrows(ClientNotFoundException.class, () -> clientService.findClientById(1L));
	}

	@Test
	void findAllClientsTest() {
		Client client1 = Client.builder().id(1L).name("First Client").build();
		Client client2 = Client.builder().id(2L).name("Second Client").build();
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
		List<Wallet> wallets = Collections.emptyList();
		Client client = Client.builder().id(1L).name("Client").wallets(wallets).build();

		given(clientRepository.findById(any(Long.class))).willReturn(Optional.of(client));
		doNothing().when(clientRepository).deleteById(any(Long.class));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientWithNullListOfWalletsTest() {
		Client client = Client.builder().id(1L).name("First Client").cpf("000.000.000-00").wallets(null).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean result = clientService.deleteClient(1L);

		assertTrue(result);
	}

	@Test
	void deleteClientPassingNullValueAsExpenseListTest() {
		Wallet wallet = Wallet.builder().id(1L).balance(BigDecimal.valueOf(50)).expenses(null).build();
		Client client = Client.builder().id(1L).name("First Client").wallets(Collections.singletonList(wallet)).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientPassingAnEmptyListAsExpenseListTest() {
		Wallet wallet = Wallet.builder().id(1L).balance(BigDecimal.valueOf(50)).expenses(Collections.emptyList()).build();
		Client client = Client.builder().id(1L).name("First Client").wallets(Collections.singletonList(wallet)).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientWithWalletContainingOnlyClosedExpensesTest() {
		Expense expense1 = Expense.builder().id(1L).expenseState(ExpenseState.CLOSED).build();
		Expense expense2 = Expense.builder().id(2L).expenseState(ExpenseState.CLOSED).build();
		Wallet wallet = Wallet.builder().id(1L).balance(BigDecimal.valueOf(8000))
				.expenses(Arrays.asList(expense1, expense2)).build();
		Client client = Client.builder().id(1L).name("First Client").wallets(Collections.singletonList(wallet)).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void shouldThrowExceptionWhenDeleteClientWithOpenExpensesTest() {
		Expense expense1 = Expense.builder().id(1L).expenseState(ExpenseState.OPEN).build();
		Expense expense2 = Expense.builder().id(2L).expenseState(ExpenseState.CLOSED).build();
		List<Expense> expenses = Arrays.asList(expense1, expense2);

		Wallet wallet = Wallet.builder().id(1L).balance(BigDecimal.valueOf(8000)).expenses(expenses).build();
		Client client = Client.builder().id(1L).name("First Client").wallets(Collections.singletonList(wallet)).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		Exception exception = assertThrows(ClientOpenedExpensesException.class, () -> clientService.deleteClient(1L));
		String expectedMessage = "Cliente com id {1} possui pendencias na carteira!";
		String exceptionMessage = exception.getMessage();

		assertThat(expectedMessage).isEqualTo(exceptionMessage);
	}

	@Test
	void changeClientPropertiesTest() {
		Client client1 = Client.builder().id(1L).name("First Client").cpf("531.521.400-10")
				.telephoneNumber("48 0 0000-0000").build();
		Client client2 = Client.builder().id(1L).name("First Client").cpf("124.327.440-98")
				.telephoneNumber("48 2 2222-2222").build();
		given(clientRepository.save(any(Client.class))).willReturn(client1, client2);

		Client savedClient = clientService.saveClient(client1);
		Client changedClient = clientService.changeClient(client2);

		assertThat(client1).isEqualTo(savedClient);
		assertThat(client2).isEqualTo(changedClient);
	}

	@Test
	void clientSaveTest() {
		Client client = Client.builder().id(1L).name("First Client").cpf("531.521.400-10")
				.telephoneNumber("48 0 0000-0000").build();
		given(clientRepository.save(any(Client.class))).willReturn(client);

		Client savedClient = clientService.saveClient(client);

		assertThat(client).isEqualTo(savedClient);
	}

	@Test
	void shouldThrowExceptionWhenClientIdIsNullTest() {
		given(clientRepository.findById(any(Long.class))).willReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> clientService.deleteClient(null));
	}

	@Test
	void shouldThrowExceptionWhenSaveClientsWithSameCpfTest() {
		Client client = Client.builder().id(1L).name("First Client").cpf("531.521.400-10")
				.telephoneNumber("48 0 0000-0000").build();
		given(clientRepository.save(client)).willThrow(DataIntegrityViolationException.class);

		DuplicateDocumentException exception = assertThrows(DuplicateDocumentException.class,
				() -> clientService.saveClient(client));

		String expectedMessage = "CPF { 531.521.400-10 } já cadastrado";
		String exceptionMessage = exception.getMessage();

		assertThat(expectedMessage).isEqualTo(exceptionMessage);
	}

	@Test
	void shouldThrowExceptionWhenClientIsNullTest() {
		Client client = null;
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(client));
		assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(client));
	}

	@Test
	void checkClientAttributesValidationOnChangeClientMethodTest() {
		Client clientWithoutCpf = Client.builder().id(1L).name("First Client").telephoneNumber("48 0 0000-0000")
				.build();
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutCpf));

		Client clientWithoutName = Client.builder().id(2L).cpf("531.521.400-10").telephoneNumber("48 0 0000-0000")
				.build();
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutName));

		Client clientWithoutTelephone = Client.builder().id(3L).name("First Client").cpf("531.521.400-10").build();
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutTelephone));
	}

	@Test
	void checkClientAttributesValidationOnSaveClientMethodTest() {
		Client clientWithoutCpf = Client.builder().id(1L).name("First Client").telephoneNumber("48 0 0000-0000")
				.build();
		assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(clientWithoutCpf));

		Client clientWithoutName = Client.builder().id(2L).cpf("531.521.400-10").telephoneNumber("48 0 0000-0000")
				.build();
		assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(clientWithoutName));

		Client clientWithoutTelephone = Client.builder().id(3L).name("First Client").cpf("531.521.400-10").build();
		assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(clientWithoutTelephone));
	}

}
