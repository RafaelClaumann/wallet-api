package br.com.application.wallet.services;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findClientById(final Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado, id: " + id, null));
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public boolean deleteClient(final Long id) {
        if (checkClientOpenedExpensesBeforeDelete(id)) {
            log.info("Cliente com id {} não pode ser deletado.", id);
            throw new IllegalArgumentException("Cliente com id {" + id + "} possui pendencias na carteira!");
        }
        log.info("Deletando cliente com id {}", id);
        clientRepository.deleteById(id);
        return true;
    }

    public Client changeClient(final Client client) {
        return clientRepository.save(client);
    }

    public Client saveClient(final Client client) {
        log.info("Salvando client. id: {}, nome: {}, cpf: {}", client.getId(),client.getName(), client.getCpf());
        return clientRepository.save(client);
    }

    /**
     * @param id
     * @return true se algum wallet do cliente possuir pelo menos uma despesa em aberto(OPEN).
     */
    private boolean checkClientOpenedExpensesBeforeDelete(final Long id) {
        Client client = this.findClientById(id);
        if(client.getWallets().isEmpty()) {
            return false;
        }
        List<Boolean> collect = client.getWallets().stream().map(this::checkWalletOpenedExpenses).collect(Collectors.toUnmodifiableList());;
        log.info("ClientId {}, Wallets {}", id, client.getWallets());
        return collect.stream().anyMatch(bool -> true);
    }

    /**
     * @param wallet
     * @return true se o wallet possuir pelo menos uma despesa em aberto(OPEN).
     */
    private boolean checkWalletOpenedExpenses(final Wallet wallet) {
        return wallet.getExpenses().stream().anyMatch(expense -> expense.getExpenseState().equals(ExpenseState.OPEN));
    }
}
