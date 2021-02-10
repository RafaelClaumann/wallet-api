package br.com.application.wallet.services;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.repositories.WalletRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
		Client client = Client.builder().id(1L).name("First Client").cpf("531.521.400-10")
				.telephoneNumber("48 0 0000-0000").build();

		given(clientService.findClientById(1L)).willReturn(client);
		given(walletRepository.save(any(Wallet.class))).willReturn(wallet);

		final Wallet savedWallet = walletService.saveWallet(1L, wallet);

		verify(walletRepository).save(wallet);
		assertThat(wallet).isEqualTo(savedWallet);
	}

	@Test
	void shouldThrowExceptionWhenTryToSaveNullWalletTest() {
		assertThrows(IllegalArgumentException.class, () -> walletService.saveWallet(1L, null));
	}

	@Test
	void shouldThrowExceptionWhenTryToSaveWalletWithoutDescriptionTest() {
		assertThrows(IllegalArgumentException.class, () -> walletService.saveWallet(1L, new Wallet()));
	}

	@Test
	void shouldThrowExceptionWhenClientIdIsNullTest() {
		Wallet wallet = Wallet.builder().id(1L).description("Carteira").balance(BigDecimal.TEN)
				.expenses(Collections.emptyList()).build();
		assertThrows(IllegalArgumentException.class, () -> walletService.saveWallet(null, wallet));
	}

	@Test
	void shouldCreateNewListOfWalletsForClientTest() {
		Wallet wallet = Wallet.builder().id(1L).description("Carteira").balance(BigDecimal.TEN)
				.expenses(Collections.emptyList()).build();
		Client client = Client.builder().id(1L).name("First Client").cpf("531.521.400-10")
				.telephoneNumber("48 0 0000-0000").wallets(null).build();

		given(clientService.findClientById(1L)).willReturn(client);
		given(walletRepository.save(any(Wallet.class))).willReturn(wallet);

		final Wallet savedWallet = walletService.saveWallet(1L, wallet);

		assertThat(wallet).isEqualTo(savedWallet);
		assertThat(client.getWallets()).isNotNull();
		assertThat(client.getWallets()).containsOnly(wallet);
	}

}
