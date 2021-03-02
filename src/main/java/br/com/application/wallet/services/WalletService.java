package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private ClientService clientService;

	public Wallet findWalletById(final Long id) {
		Optional<Wallet> wallet = walletRepository.findById(id);

		if(wallet.isPresent()) {
			if(Objects.isNull(wallet.get().getExpenses())) {
				wallet.get().setExpenses(new ArrayList<>());
			}
			return wallet.get();
		}

		throw new WalletNotFoundException("Carteira, id: {" + id +"} não encontrada");
	}

	public List<Wallet> findAllWallets() {
		return walletRepository.findAll();
	}

	public boolean deleteWallet(final Long id) {
		final Wallet wallet = this.findWalletById(id);

		if(Objects.isNull(wallet.getExpenses()) || wallet.getExpenses().isEmpty())
			throw new WalletNotFoundException("Carteira, id: {" + id +"} sem despesas.");

		if (hasOpenedExpensesInWallet(wallet))
			throw new OpenedExpensesException("Carteira, id: {" + id + "} possui despesas em aberto");

		walletRepository.delete(wallet);
		return true;
	}

	public Wallet saveWallet(final Long clientId, final Wallet wallet) {
		if (Objects.isNull(wallet) || Objects.isNull(wallet.getDescription()))
			throw new IllegalArgumentException("Carteira inválida");

		if (Objects.isNull(clientId))
			throw new IllegalArgumentException("Id inválido");

		final Client client = clientService.findClientById(clientId);

		if (Objects.isNull(client.getWallets()))
			client.setWallets(new ArrayList<>());

		if (client.getWallets().contains(wallet))
			throw new IllegalArgumentException("Esta Carteira ja foi cadastrada!");

		client.getWallets().add(wallet);

		return walletRepository.save(wallet);
	}

	private boolean hasOpenedExpensesInWallet(final Wallet wallet) {
		final List<Expense> expenses = wallet.getExpenses();
		final Optional<Expense> first = expenses.stream()
				.filter(expense -> expense.getExpenseState().equals(ExpenseState.OPEN)).findFirst();
		return first.isPresent();
	}
}
