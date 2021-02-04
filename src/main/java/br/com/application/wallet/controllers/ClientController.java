package br.com.application.wallet.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.dto.ClientDTO;
import br.com.application.wallet.models.dto.ClientForm;
import br.com.application.wallet.services.ClientService;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/wallet/v1/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@GetMapping("/{id_client}")
	public ResponseEntity<ClientDTO> findClientById(@PathVariable("id_client") long idClient) {
		Client foundClient = clientService.findClientById(idClient);
		return ResponseEntity.ok().body(new ClientDTO(foundClient));
	}

	@GetMapping
	public ResponseEntity<List<ClientDTO>> findAllClients() {
		List<Client> findAllClients = clientService.findAllClients();
		List<ClientDTO> dtos = findAllClients.stream().map(ClientDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(dtos);
	}

	@PostMapping
	public ResponseEntity<ClientDTO> saveClient(@Valid @RequestBody ClientForm clientForm, UriComponentsBuilder uriBuilder) {
		Client client = clientForm.convertFormToClient(clientForm);
		clientService.saveClient(client);

		URI resourceLocation = uriBuilder.path("/wallet/v1/clients/{id_client}").buildAndExpand(client.getId()).toUri();

		return ResponseEntity.created(resourceLocation).body(new ClientDTO(client));
	}
}
