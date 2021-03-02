package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.application.wallet.mocks.MockWallet.*;
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
		Wallet wallet = mockSingleWallet(1L, Collections.emptyList());

		given(walletRepository.findById(any(Long.class))).willReturn(Optional.of(wallet));

		final Wallet foundWallet = walletService.findWalletById(1L);

		assertThat(foundWallet).isEqualTo(wallet);
	}

/*	@Test
	void shouldInstantiateExpensesListForAGivenWalletTest() {
		Wallet wallet = new Wallet();
		wallet.setExpenses(null);

		given(walletRepository.findById(any(Long.class))).willReturn(Optional.of(wallet));

		final Wallet foundWallet = walletService.findWalletById(1L);

		assertThat(wallet.getExpenses()).isNull();
		assertThat(foundWallet.getExpenses()).isNotNull();
	}*/

	@Test
	void shouldThrowAnExceptionWhenWalletNotFoundByIdTest() {
		given(walletRepository.findById(any(Long.class))).willReturn(Optional.empty());

		assertThrows(WalletNotFoundException.class, () -> walletService.findWalletById(1L));
	}

	@Test
	void shouldReturnAListOfWalletsTest() {
		Wallet wallet1 = mockSingleWalletWithOpenedExpenses(1L);
		Wallet wallet2 = mockSingleWalletWithClosedExpenses(2L);
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
		Wallet wallet = mockSingleWallet(1L);
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
		Wallet wallet = mockSingleWallet(1L);
		assertThrows(IllegalArgumentException.class, () -> walletService.saveWallet(null, wallet));
	}

	@Test
	void shouldCreateNewListOfWalletsForClientTest() {
		Wallet wallet = mockSingleWallet(1L);
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
