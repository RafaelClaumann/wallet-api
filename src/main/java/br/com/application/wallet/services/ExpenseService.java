package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.models.ExpenseEntity;
import br.com.application.wallet.models.WalletEntity;
import br.com.application.wallet.models.api.Data;
import br.com.application.wallet.models.dto.form.ExpenseForm;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.models.enums.ExpenseType;
import br.com.application.wallet.repositories.ExpenseRepository;
import br.com.application.wallet.repositories.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private WalletRepository walletRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseService.class);

    public ResponseEntity<Data<ExpenseEntity>> saveExpense(final Long walletId, final ExpenseForm expenseForm) {

        WalletEntity wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Carteira não encontrada"));

        if (Objects.isNull(wallet.getExpenses()) || wallet.getExpenses().isEmpty())
            wallet.setExpenses(new ArrayList<>());

        final Optional<ExpenseType> expenseTypeById = ExpenseType.getExpenseTypeById(expenseForm.getExpenseTypeId());
        final Optional<ExpenseState> expenseStateById = ExpenseState.getExpenseStateById(expenseForm.getExpenseStateId());

        final ExpenseEntity expenseEntity = ExpenseEntity.builder()
                .description(expenseForm.getDescription())
                .value(expenseForm.getValue())
                .expenseType(expenseTypeById.get())
                .expenseState(expenseStateById.get())
                .build();

        expenseRepository.save(expenseEntity);
        wallet.getExpenses().add(expenseEntity);

        LOGGER.info(expenseEntity.toString());
        LOGGER.info(expenseForm.toString());
        return new ResponseEntity<>(new Data<>(expenseEntity), HttpStatus.CREATED);
    }
}
