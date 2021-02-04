package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.ClientOpenedExpensesException;
import br.com.application.wallet.handler.exceptions.DuplicateDocumentException;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientService {

	@Autowired private ClientRepository clientRepository;

	public Client findClientById(final Long id) {
		Optional<Client> client = clientRepository.findById(id);
		log.info("Buscando cliente com id {}.", id);
		return client.orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado, id: " + id));
	}

	public List<Client> findAllClients() {
		return clientRepository.findAll();
	}

	public boolean deleteClient(final Long id) {
		log.info("Tentando deletar cliente com id {}.", id);
		if (Objects.isNull(id)) {
			throw new IllegalArgumentException("É preciso informar um id válido. ID informado: {" + id + "}");
		}
		if (checkClientOpenedExpensesBeforeDelete(id)) {
			log.info("Cliente com id {} não pode ser deletado.", id);
			throw new ClientOpenedExpensesException("Cliente com id {" + id + "} possui pendencias na carteira!");
		}
		log.info("Deletando cliente com id {}", id);
		clientRepository.deleteById(id);
		return true;
	}

	public Client changeClient(final Client client) {
		return clientRepository.save(client);
	}

	public Client saveClient(final Client client) {
		try {
			log.info("Salvando client, nome: {}, cpf: {}", client.getName(), client.getCpf());
			return clientRepository.save(client);
		} catch (DataIntegrityViolationException exception) {
			log.info("CPF {} já cadastrado no sistema", client.getCpf());
			throw new DuplicateDocumentException("CPF { " + client.getCpf() + " } já cadastrado");
		}
	}

	/**
	 * @param id
	 * @return true se algum wallet do cliente possuir pelo menos uma despesa em aberto(OPEN).
	 */
	private boolean checkClientOpenedExpensesBeforeDelete(final Long id) {
		Client client = this.findClientById(id);
		System.out.println("ERRO" + client);
		if (Objects.isNull(client.getWallets()) || client.getWallets().isEmpty()) {
			return false;
		}
		List<Boolean> collect = client.getWallets().stream().map(this::checkWalletOpenedExpenses)
				.collect(Collectors.toUnmodifiableList());

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
