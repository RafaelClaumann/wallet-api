package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.DuplicateDocumentException;
import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	public Client findClientById(final Long id) {
		Optional<Client> client = clientRepository.findById(id);

		if (client.isPresent()) {
			if (Objects.isNull(client.get().getWallets())) {
				client.get().setWallets(new ArrayList<>());
			}
			return client.get();
		}

		throw new ClientNotFoundException("Cliente não encontrado, id: " + id);
	}

	public List<Client> findAllClients() {
		return clientRepository.findAll();
	}

	public boolean deleteClient(final Long id) {

		if (checkClientOpenedExpensesBeforeDelete(id)) {
			throw new OpenedExpensesException("Cliente com id {" + id + "} possui pendencias na carteira!");
		}

		clientRepository.deleteById(id);
		return true;
	}

	public Client changeClient(final Client client) {
		if (Objects.isNull(client) || Objects.isNull(client.getName()) || Objects.isNull(client.getCpf()) || Objects
				.isNull(client.getTelephoneNumber())) {
			throw new IllegalArgumentException("Cliente inválido");
		}

		return clientRepository.save(client);
	}

	public Client saveClient(final Client client) {
		try {
			return clientRepository.save(client);
		} catch (DataIntegrityViolationException exception) {
			throw new DuplicateDocumentException("CPF { " + client.getCpf() + " } já cadastrado");
		}
	}

	/**
	 * @param id
	 * @return true se algum wallet do cliente possuir pelo menos uma despesa em aberto(OPEN).
	 */
	private boolean checkClientOpenedExpensesBeforeDelete(final Long id) {
		Client client = this.findClientById(id);

		List<Boolean> collect = client.getWallets().stream().map(this::checkWalletOpenedExpenses)
				.collect(Collectors.toUnmodifiableList());

		return collect.stream().anyMatch(bool -> bool.equals(Boolean.TRUE));
	}

	/**
	 * @param wallet
	 * @return true se o wallet possuir pelo menos uma despesa em aberto(OPEN).
	 */
	private boolean checkWalletOpenedExpenses(final Wallet wallet) {
		if (Objects.isNull(wallet.getExpenses()) || wallet.getExpenses().isEmpty())
			return false;

		final boolean hasOpenedExpenses = wallet.getExpenses().stream()
				.anyMatch(expense -> expense.getExpenseState().equals(ExpenseState.OPEN));
		return hasOpenedExpenses;
	}
}
