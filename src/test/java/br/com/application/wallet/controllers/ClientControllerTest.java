package br.com.application.wallet.controllers;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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

	@BeforeEach
	void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void findClientByIdTest() throws Exception {
		long clientId = 1L;
		Client client = Client.builder().id(clientId).name("Rafael Claumann").build();
		given(clientService.findClientById(1L)).willReturn(client);

		MockHttpServletResponse response = mockMvc.perform(get("/wallet/v1/clients/{id_client}", "1")).andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat((response.getContentAsString())).isEqualTo(json.write(new ClientDTO(client)).getJson());
	}

	@Test
	void findAllClientsTest() throws Exception {
		Client client0 = Client.builder().id(1L).name("Client0").build();
		Client client1 = Client.builder().id(2L).name("Client1").build();
		Client client2 = Client.builder().id(3L).name("Client2").build();
		List<Client> clients = asList(client0, client1, client2);
		List<ClientDTO> expectedClients = clients.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
		
		given(clientService.findAllClients()).willReturn(clients);

		MockHttpServletResponse response = mockMvc.perform(get("/wallet/v1/clients/")).andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(jsonList.write(expectedClients).getJson());
	}

}
