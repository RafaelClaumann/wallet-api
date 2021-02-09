package br.com.application.wallet.services;

import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.repositories.WalletRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private ClientService clientService;

	public Wallet findWalletById(final Long id) {
		Optional<Wallet> wallet = walletRepository.findById(id);
		return wallet.orElseThrow(() -> new ObjectNotFoundException("Wallet não encontrada", null));
	}
}
