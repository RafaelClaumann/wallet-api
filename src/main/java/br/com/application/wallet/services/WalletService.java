package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.models.ClientEntity;
import br.com.application.wallet.models.ExpenseEntity;
import br.com.application.wallet.models.WalletEntity;
import br.com.application.wallet.models.api.Data;
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

    public ResponseEntity<Data<WalletDTO>> findWalletById(final Long id) {
        WalletEntity wallet = walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Carteira, id: {" + id + "} não encontrada"));

        if (Objects.isNull(wallet.getExpenses()))
            wallet.setExpenses(new ArrayList<>());

        return new ResponseEntity<>(new Data<>(new WalletDTO(wallet)), HttpStatus.OK);
    }

    public List<WalletEntity> findAllWallets() {
        return walletRepository.findAll();
    }

    public boolean deleteWallet(final Long id) {
        final WalletEntity wallet = walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Carteira, id: {" + id + "} não encontrada"));

        if (Objects.isNull(wallet.getExpenses()) || wallet.getExpenses().isEmpty()) {
            walletRepository.delete(wallet);
            return true;
        }

        if (hasOpenedExpensesInWallet(wallet))
            throw new OpenedExpensesException("Carteira, id: {" + id + "} possui despesas em aberto");

        walletRepository.delete(wallet);
        return true;
    }

    public ResponseEntity<Data<WalletDTO>> saveWallet(final Long clientId, final WalletForm form) {

        final ClientEntity client = clientService.findClientById(clientId);

        final WalletEntity wallet = walletRepository.save(WalletEntity.builder()
                .description(form.getDescription())
                .balance(form.getBalance())
                .expenses(new ArrayList<>())
                .build());

        client.setWallet(wallet);

        return new ResponseEntity<>(new Data<>(new WalletDTO(wallet)), HttpStatus.CREATED);
    }

    private boolean hasOpenedExpensesInWallet(final WalletEntity wallet) {
        final List<ExpenseEntity> expenses = wallet.getExpenses();
        final Optional<ExpenseEntity> first = expenses.stream()
                .filter(expense -> expense.getExpenseState().equals(ExpenseState.OPEN)).findFirst();
        return first.isPresent();
    }
}
