package br.com.application.wallet.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.dto.ClientDTO;
import br.com.application.wallet.services.ClientService;

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
		List<ClientDTO> dtos = findAllClients.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
		return ResponseEntity.ok().body(dtos);
	}
}
