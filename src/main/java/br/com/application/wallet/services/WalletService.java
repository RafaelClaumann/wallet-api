package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.api.Data;
import br.com.application.wallet.models.dto.ClientDTO;
import br.com.application.wallet.models.dto.WalletDTO;
import br.com.application.wallet.models.dto.form.WalletForm;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        if (wallet.isPresent()) {
            if (Objects.isNull(wallet.get().getExpenses())) {
                wallet.get().setExpenses(new ArrayList<>());
            }
            return wallet.get();
        }

        throw new WalletNotFoundException("Carteira, id: {" + id + "} não encontrada");
    }

    public List<Wallet> findAllWallets() {
        return walletRepository.findAll();
    }

    public boolean deleteWallet(final Long id) {
        final Wallet wallet = this.findWalletById(id);

        if (hasOpenedExpensesInWallet(wallet))
            throw new OpenedExpensesException("Carteira, id: {" + id + "} possui despesas em aberto");

        walletRepository.delete(wallet);
        return true;
    }

    public ResponseEntity<Data<WalletDTO>> saveWallet(final Long clientId, final WalletForm form) {

        final Client client = clientService.findClientById(clientId);

        if (Objects.isNull(client.getWallets()))
            client.setWallets(new ArrayList<>());

        final Wallet wallet = walletRepository.save(Wallet.builder()
                .description(form.getDescription())
                .balance(form.getBalance())
                .expenses(new ArrayList<>())
                .build());

        client.getWallets().add(wallet);

        return new ResponseEntity<>(new Data<>(new WalletDTO(wallet)), HttpStatus.OK);
    }

    private boolean hasOpenedExpensesInWallet(final Wallet wallet) {
        final List<Expense> expenses = wallet.getExpenses();
        final Optional<Expense> first = expenses.stream()
                .filter(expense -> expense.getExpenseState().equals(ExpenseState.OPEN)).findFirst();
        return first.isPresent();
    }
}
