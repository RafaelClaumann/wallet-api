package br.com.application.wallet.controllers;

import br.com.application.wallet.models.ExpenseEntity;
import br.com.application.wallet.models.api.Data;
import br.com.application.wallet.models.dto.form.ExpenseForm;
import br.com.application.wallet.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wallet/v1/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/{id_wallet}")
    public ResponseEntity<Data<ExpenseEntity>> saveExpense(
            @PathVariable("id_wallet") final Long idWallet,
            @Valid @RequestBody ExpenseForm expenseForm) {

        return expenseService.saveExpense(idWallet, expenseForm);
    }
}
