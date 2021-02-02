package br.com.application.wallet.controllers;

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
	private ResponseEntity<ClientDTO> findClientById(@PathVariable("id_client") long idClient) {
		Client foundClient = clientService.findClientById(idClient);
		return ResponseEntity.ok().body(new ClientDTO(foundClient));
	}
	
}
