package br.com.application.wallet.services;

import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.repositories.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private ClientService clientService;

	public Wallet findWalletById(final Long id) {
		log.info("Buscando carteira com id {}.", id);
		Optional<Wallet> wallet = walletRepository.findById(id);
		return wallet.orElseThrow(() -> new ObjectNotFoundException("Carteira não encontrada", null));
	}

	public List<Wallet> findAllWallets() {
		log.info("Listando as carteiras cadastradas.");
		return walletRepository.findAll();
	}

	public Wallet saveWallet(Wallet wallet) {
		if(Objects.isNull(wallet) || Objects.isNull(wallet.getDescription()))
			throw new IllegalArgumentException("Carteira inválida");
		log.info("Salvando carteira, descrição: {}, saldo: {}.", wallet.getDescription(), wallet.getBalance());
		return walletRepository.save(wallet);
	}
}
