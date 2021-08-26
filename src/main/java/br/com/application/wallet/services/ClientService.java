package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.DuplicateDocumentException;
import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.models.ClientEntity;
import br.com.application.wallet.models.WalletEntity;
import br.com.application.wallet.models.api.Data;
import br.com.application.wallet.models.dto.ClientDTO;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	public ClientEntity findClientById(final Long id) {
		Optional<ClientEntity> client = clientRepository.findById(id);

		if (client.isPresent()) {
			return client.get();
		}

		throw new ClientNotFoundException("Cliente não encontrado, id: " + id);
	}

	public ResponseEntity<Data<List<ClientDTO>>> findAllClients() {
		final List<ClientEntity> clientList = clientRepository.findAll();

		if(clientList.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		final List<ClientDTO> clientDTOS = ClientDTO.convertListToDTO(clientList);

		return new ResponseEntity<>(new Data<>(clientDTOS), HttpStatus.OK);
	}

	public boolean deleteClient(final Long id) {

		if (checkClientOpenedExpensesBeforeDelete(id)) {
			throw new OpenedExpensesException("Cliente com id {" + id + "} possui pendencias na carteira!");
		}

		clientRepository.deleteById(id);
		return true;
	}

	public ClientEntity changeClient(final ClientEntity client) {
		if (Objects.isNull(client) || Objects.isNull(client.getName()) || Objects.isNull(client.getCpf()) || Objects
				.isNull(client.getTelephoneNumber())) {
			throw new IllegalArgumentException("Cliente inválido");
		}

		return clientRepository.save(client);
	}

	public ClientEntity saveClient(final ClientEntity client) {
		try {
			return clientRepository.save(client);
		} catch (DataIntegrityViolationException exception) {
			throw new DuplicateDocumentException("CPF { " + client.getCpf() + " } já cadastrado");
		}
	}

	/**
	 * @param id
	 * @return a wallet do cliente possuir pelo menos uma despesa em aberto(OPEN).
	 */
	private boolean checkClientOpenedExpensesBeforeDelete(final Long id) {
		WalletEntity wallet = this.findClientById(id).getWallet();

		if(Objects.isNull(wallet) || Objects.isNull(wallet.getExpenses()) || wallet.getExpenses().isEmpty())
			return false;

		return wallet.getExpenses().stream().anyMatch(expense -> expense.getExpenseState().equals(ExpenseState.OPEN));
	}

}
