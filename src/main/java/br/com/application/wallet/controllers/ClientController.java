package br.com.application.wallet.controllers;

import br.com.application.wallet.models.ClientEntity;
import br.com.application.wallet.models.api.Data;
import br.com.application.wallet.models.dto.*;
import br.com.application.wallet.models.dto.form.ClientForm;
import br.com.application.wallet.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/wallet/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/{id_client}/wallets/{id_wallet}")
    public ResponseEntity<ClientWalletExpensesDTO> findClientWithWalletExpenses(
            @PathVariable("id_client") final Long idClient, @PathVariable("id_wallet") final Long idWallet) {
        final ClientEntity client = clientService.findClientById(idClient);
        return ResponseEntity.ok(new ClientWalletExpensesDTO(client, idWallet));
    }

    @GetMapping("/{id_client}/wallets")
    public ResponseEntity<ClientWalletsDTO> findClientWithWallets(@PathVariable("id_client") final Long idClient) {
        final ClientEntity client = clientService.findClientById(idClient);
        return ResponseEntity.ok(new ClientWalletsDTO(client));
    }

    @GetMapping("/{id_client}")
    public ResponseEntity<ClientDTO> findClientById(@PathVariable("id_client") final Long idClient) {
        ClientEntity foundClient = clientService.findClientById(idClient);
        return ResponseEntity.ok().body(new ClientDTO(foundClient));
    }

    @GetMapping
    public ResponseEntity<Data<List<ClientDTO>>> findAllClients() {
        return clientService.findAllClients();
    }

    @PostMapping
    public ResponseEntity<ClientDTO> saveClient(@Valid @RequestBody final ClientForm clientForm,
                                                final UriComponentsBuilder uriBuilder) {
        ClientEntity client = clientForm.convertFormToClient(clientForm);
        clientService.saveClient(client);

        URI resourceLocation = uriBuilder.path("/wallet/v1/clients/{id_client}").buildAndExpand(client.getId()).toUri();

        return ResponseEntity.created(resourceLocation).body(new ClientDTO(client));
    }

    @DeleteMapping("/{id_client}")
    public ResponseEntity<?> deleteClient(@PathVariable("id_client") final Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
