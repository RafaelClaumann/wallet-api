package br.com.application.wallet.controllers;

import br.com.application.wallet.models.api.Data;
import br.com.application.wallet.models.dto.WalletDTO;
import br.com.application.wallet.models.dto.form.WalletForm;
import br.com.application.wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wallet/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/{id_client}")
    public ResponseEntity<Data<WalletDTO>> saveWallet(
            @PathVariable("id_client") Long idClient,
            @Valid @RequestBody WalletForm walletForm) {

        return walletService.saveWallet(idClient, walletForm);
    }
}
