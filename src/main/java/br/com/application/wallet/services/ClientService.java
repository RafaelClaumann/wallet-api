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

    public Client findById(final Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado, id: " + id, null));
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public boolean deleteClient(Long id) {
        if (checkClientOpenExpensesBeforeDelete(id)) {
            log.info("Cliente com id {} não pode ser deletado.", id);
            throw new IllegalArgumentException("Cliente com id {" + id + "} possui pendencias na carteira!");
        }
        log.info("Deletando cliente com id {}", id);
        clientRepository.deleteById(id);
        return true;
    }

    /**
     * @param id
     * @return true se algum wallet do cliente possuir pelo menos uma despesa em aberto(OPEN).
     */
    private boolean checkClientOpenExpensesBeforeDelete(final Long id) {
        Client client = this.findById(id);
        List<Boolean> collect = client.getWallets().stream().map(this::checkWalletOpenExpenses).collect(Collectors.toUnmodifiableList());;
        log.info("ClientId {}, Wallets {}", id, client.getWallets());
        return collect.stream().anyMatch(bool -> true);
    }

    /**
     * @param wallet
     * @return true se o wallet possuir pelo menos uma despesa em aberto(OPEN).
     */
    private boolean checkWalletOpenExpenses(final Wallet wallet) {
        return wallet.getExpenses().stream().anyMatch(a -> a.getExpenseState() == ExpenseState.OPEN);
    }
}
