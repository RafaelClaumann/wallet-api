package br.com.application.wallet.controllers;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.dto.ClientDTO;
import br.com.application.wallet.models.dto.ClientForm;
import br.com.application.wallet.models.dto.ClientWalletsDTO;
import br.com.application.wallet.models.dto.WalletDTO;
import br.com.application.wallet.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wallet/v1/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@GetMapping("/{id_client}/wallets")
	public ResponseEntity<ClientWalletsDTO> findClientWithWallets(@PathVariable("id_client") final Long idClient) {
		final Client client = clientService.findClientById(idClient);
		final List<Wallet> wallets = client.getWallets();

		final List<WalletDTO> collect = wallets.stream().map(WalletDTO::new).collect(Collectors.toList());
		final ClientWalletsDTO clientWalletsDTO = new ClientWalletsDTO(client.getId(), client.getName(), collect);

		return ResponseEntity.ok(clientWalletsDTO);
	}

	@GetMapping("/{id_client}")
	public ResponseEntity<ClientDTO> findClientById(@PathVariable("id_client") final Long idClient) {
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
	public ResponseEntity<ClientDTO> saveClient(@Valid @RequestBody final ClientForm clientForm,
			final UriComponentsBuilder uriBuilder) {
		Client client = clientForm.convertFormToClient(clientForm);
		clientService.saveClient(client);

		URI resourceLocation = uriBuilder.path("/wallet/v1/clients/{id_client}").buildAndExpand(client.getId()).toUri();

		return ResponseEntity.created(resourceLocation).body(new ClientDTO(client));
	}

	@DeleteMapping("/{id_client}")
	public ResponseEntity deleteClient(@PathVariable("id_client") final Long id) {
		clientService.deleteClient(id);
		return ResponseEntity.noContent().build();
	}

}
