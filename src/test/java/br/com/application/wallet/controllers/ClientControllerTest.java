package br.com.application.wallet.controllers;

import static br.com.application.wallet.mocks.MockClient.mockSingleClient;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.com.application.wallet.handler.exceptions.ClientOpenedExpensesException;
import br.com.application.wallet.mocks.MockClient;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.dto.ClientWalletExpensesDTO;
import br.com.application.wallet.models.dto.ClientWalletsDTO;
import br.com.application.wallet.models.enums.ExpenseState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.dto.ClientDTO;
import br.com.application.wallet.services.ClientService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClientController.class)
class ClientControllerTest {

	@MockBean
	private ClientService clientService;

	@Autowired
	private MockMvc mockMvc;

	private JacksonTester<ClientDTO> json;
	private JacksonTester<List<ClientDTO>> jsonList;
	private JacksonTester<ClientWalletsDTO> jsonClientWalletsDTO;
	private JacksonTester<ClientWalletExpensesDTO> jsonClientWalletExpensesDTO;

	@BeforeEach
	void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void findClientByIdTest() throws Exception {
		Client client = mockSingleClient(1L);
		given(clientService.findClientById(1L)).willReturn(client);

		MockHttpServletResponse response = mockMvc.perform(get("/wallet/v1/clients/{id_client}", "1")).andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat((response.getContentAsString())).isEqualTo(json.write(new ClientDTO(client)).getJson());
	}

	@Test
	void findAllClientsTest() throws Exception {
		Client client0 = mockSingleClient(1L);
		Client client1 = mockSingleClient(2L);
		Client client2 = mockSingleClient(3L);
		List<Client> clients = asList(client0, client1, client2);
		List<ClientDTO> expectedClients = clients.stream().map(ClientDTO::new).collect(Collectors.toList());

		given(clientService.findAllClients()).willReturn(clients);

		MockHttpServletResponse response = mockMvc.perform(get("/wallet/v1/clients/")).andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(jsonList.write(expectedClients).getJson());
	}

	@Test
	void findAllClientsShouldReturnAnEmptyList() throws Exception {
		given(clientService.findAllClients()).willReturn(Collections.emptyList());

		MockHttpServletResponse response = mockMvc.perform(get("/wallet/v1/clients/")).andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	void shouldPostNewClientTest() throws Exception {
		Client client = mockSingleClient(1L);
		client.setId(null);
		ClientDTO clientDTO = new ClientDTO(client);

		given(clientService.saveClient(client)).willReturn(client);

		MockHttpServletResponse response = mockMvc.perform(
				post("/wallet/v1/clients").contentType(MediaType.APPLICATION_JSON)
						.content(json.write(clientDTO).getJson())).andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.getContentAsString()).isEqualTo(json.write(clientDTO).getJson());
	}

	@Test
	void deleteClientShouldReturnHttpNoContentTest() throws Exception {
		given(clientService.deleteClient(any(Long.class))).willReturn(true);

		MockHttpServletResponse response = mockMvc.perform(delete("/wallet/v1/clients/{id_client}", "1")).andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(response.getContentAsString()).isEmpty();
	}

	@Test
	void deleteClientShouldReturnHttpBadRequestTest() throws Exception {
		given(clientService.deleteClient(any(Long.class))).willThrow(ClientOpenedExpensesException.class);

		final MockHttpServletResponse response = mockMvc.perform(delete("/wallet/v1/clients/{id_client}", "1"))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void shouldReturnClientWalletsListTest() throws Exception {
		Client client = mockSingleClient(1l);

		given(clientService.findClientById(any(Long.class))).willReturn(client);

		MockHttpServletResponse response = mockMvc.perform(get("/wallet/v1/clients/{id_client}/wallets", "1"))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(jsonClientWalletsDTO.write(new ClientWalletsDTO(client)).getJson());
	}

	@Test
	void shouldReturnClientWalletsExpensesListTest() throws Exception {
		Client client = mockSingleClient(1L);

		given(clientService.findClientById(any(Long.class))).willReturn(client);
		System.out.println(client);

		MockHttpServletResponse response = mockMvc.perform(
				get("/wallet/v1/clients/{id_client}/wallets/{id_wallet}", "1", "1"))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString())
				.isEqualTo(jsonClientWalletExpensesDTO.write(new ClientWalletExpensesDTO(client, 1L)).getJson());
	}

}
