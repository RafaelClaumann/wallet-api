package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.handler.exceptions.DuplicateDocumentException;
import br.com.application.wallet.mocks.MockWallet;
import br.com.application.wallet.models.ClientEntity;
import br.com.application.wallet.models.ExpenseEntity;
import br.com.application.wallet.models.WalletEntity;
import br.com.application.wallet.models.api.Data;
import br.com.application.wallet.models.dto.ClientDTO;
import br.com.application.wallet.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
		ClientEntity client = mockSingleClient(1L);
		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		ClientEntity foundClient = clientService.findClientById(1L);

		verify(clientRepository).findById(1L);
		assertThat(foundClient).isEqualTo(client);
	}

	@Test
	void shouldThrowExceptionWhenClientNotFoundTest() {
		given(clientRepository.findById(any(Long.class))).willReturn(Optional.empty());

		assertThrows(ClientNotFoundException.class, () -> clientService.findClientById(1L));
	}

	@Test
	void findAllClientsSuccessTest() {
		ClientEntity client1 = mockSingleClient(1L);
		ClientEntity client2 = mockSingleClient(2L);
		List<ClientEntity> clients = Arrays.asList(client1, client2);

		given(clientRepository.findAll()).willReturn(clients);

		final ResponseEntity<Data<List<ClientDTO>>> foundClients = clientService.findAllClients();

		final Long expectedId = clients.get(1).getId();
		final Long foundId = foundClients.getBody().getData().get(1).getId();
		final List<ClientDTO> clientDTOS = ClientDTO.convertListToDTO(clients);

		verify(clientRepository).findAll();
		assertThat(expectedId).isEqualTo(foundId);
		assertThat(clientDTOS).isEqualTo(foundClients.getBody().getData());
	}

	@Test
	void findAllClientsNoContentTest() {
		given(clientRepository.findAll()).willReturn(Collections.emptyList());

		final ResponseEntity<Data<List<ClientDTO>>> foundClients = clientService.findAllClients();

		verify(clientRepository).findAll();
		assertThat(foundClients.getBody()).isNull();
		assertThat(foundClients.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void deleteClientWithAnEmptyListOfWalletsTest() {
		ClientEntity client = mockSingleClient(1L);
		client.setWallet(new WalletEntity());

		given(clientRepository.findById(any(Long.class))).willReturn(Optional.of(client));
		doNothing().when(clientRepository).deleteById(any(Long.class));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientWithNullListOfWalletsTest() {
		ClientEntity client = mockSingleClient(1L);
		client.setWallet(null);

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean result = clientService.deleteClient(1L);

		assertTrue(result);
	}

	@Test
	void deleteClientPassingNullValueAsExpenseListTest() {
		ClientEntity client = mockSingleClient(1L);
		client.getWallet().setExpenses(null);

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientPassingAnEmptyListAsExpenseListTest() {
		ClientEntity client = mockSingleClient(1L);
		client.getWallet().setExpenses(Collections.emptyList());

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void deleteClientWithWalletContainingOnlyClosedExpensesTest() {
		WalletEntity wallet = MockWallet.mockSingleWalletWithClosedExpenses(1L);
		ClientEntity client = ClientEntity.builder().id(1L).name("First Client").wallet(wallet).build();

		given(clientRepository.findById(1L)).willReturn(Optional.of(client));

		boolean isDeleted = clientService.deleteClient(1L);

		assertThat(isDeleted).isTrue();
	}

	@Test
	void shouldThrowExceptionWhenDeleteClientWithOpenExpensesTest() {
		List<ExpenseEntity> expenses = mockMixedOpenClosedExpensesList();
		WalletEntity wallet = MockWallet.mockSingleWallet(1L, expenses);
		ClientEntity client = ClientEntity.builder().id(1L).name("First Client").wallet(wallet).build();

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

		ClientEntity client = mockSingleClient(1L, cpf);
		ClientEntity modifiedClient = mockSingleClient(1L, modifiedCpf);

		given(clientRepository.save(any(ClientEntity.class))).willReturn(client, modifiedClient);

		ClientEntity savedClient = clientService.saveClient(client);
		ClientEntity changedClient = clientService.changeClient(modifiedClient);

		assertThat(client).isEqualTo(savedClient);
		assertThat(modifiedClient).isEqualTo(changedClient);
	}

	@Test
	void clientSaveTest() {
		ClientEntity client = mockSingleClient(1L, "531.521.400-10");

		given(clientRepository.save(any(ClientEntity.class))).willReturn(client);

		ClientEntity savedClient = clientService.saveClient(client);

		verify(clientRepository).save(client);
		assertThat(client).isEqualTo(savedClient);
	}

	@Test
	void shouldThrowExceptionWhenSaveClientsWithSameCpfTest() {
		ClientEntity client = mockSingleClient(1L, "531.521.400-10");

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
		ClientEntity clientWithoutCpf = mockSingleClient(1L);
		clientWithoutCpf.setCpf(null);
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutCpf));

		ClientEntity clientWithoutName = mockSingleClient(2L);
		clientWithoutName.setName(null);
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutName));

		ClientEntity clientWithoutTelephone = mockSingleClient(3L);
		clientWithoutTelephone.setTelephoneNumber(null);
		assertThrows(IllegalArgumentException.class, () -> clientService.changeClient(clientWithoutTelephone));
	}

}
