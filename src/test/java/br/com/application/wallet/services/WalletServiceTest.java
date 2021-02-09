package br.com.application.wallet.services;

import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.repositories.WalletRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class WalletServiceTest {

	@InjectMocks
	private WalletService walletService;

	@Mock
	private WalletRepository walletRepository;

	@Mock
	private ClientService clientService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void shouldReturnWalletByIdTest() {
		Wallet wallet = Wallet.builder().id(1L).description("Carteira").balance(BigDecimal.TEN)
				.expenses(Collections.emptyList()).build();
		given(walletRepository.findById(any(Long.class))).willReturn(Optional.of(wallet));

		final Wallet foundWallet = walletService.findWalletById(1L);

		assertThat(foundWallet).isEqualTo(wallet);
	}

	@Test
	void shouldThrowAnExceptionWhenWalletNotFoundByIdTest() {
		given(walletRepository.findById(any(Long.class))).willReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> walletService.findWalletById(1L));
	}

	@Test
	void shouldReturnAListOfWalletsTest() {
		Wallet wallet1 = Wallet.builder().id(1L).description("Carteira1").build();
		Wallet wallet2 = Wallet.builder().id(2L).description("Carteira2").build();
		final List<Wallet> wallets = Arrays.asList(wallet1, wallet2);

		given(walletRepository.findAll()).willReturn(wallets);

		final List<Wallet> allWallets = walletService.findAllWallets();

		assertThat(allWallets).isEqualTo(wallets);
	}

	@Test
	void shouldReturnAnEmptyListOfWalletsTest() {
		given(walletRepository.findAll()).willReturn(Collections.emptyList());

		final List<Wallet> allWallets = walletService.findAllWallets();

		assertThat(allWallets).isEmpty();
	}

	@Test
	void shouldSaveAWalletTest() {
		Wallet wallet = Wallet.builder().id(1L).description("Carteira").balance(BigDecimal.TEN)
				.expenses(Collections.emptyList()).build();

		given(walletRepository.save(any(Wallet.class))).willReturn(wallet);

		final Wallet savedWallet = walletService.saveWallet(wallet);

		assertThat(wallet).isEqualTo(savedWallet);
	}

	@Test
	void shouldThrowExceptionWhenTryToSaveNullWalletTest() {
		given(walletRepository.save(null)).willThrow(IllegalArgumentException.class);

		assertThrows(IllegalArgumentException.class, () -> walletService.saveWallet(null));
	}

	@Test
	void shouldThrowExceptionWhenSaveWalletWithoutDescriptionTest() {
		Wallet wallet = new Wallet();

		given(walletRepository.save(wallet)).willThrow(IllegalArgumentException.class);

		assertThrows(IllegalArgumentException.class, () -> walletService.saveWallet(wallet));
	}

}
