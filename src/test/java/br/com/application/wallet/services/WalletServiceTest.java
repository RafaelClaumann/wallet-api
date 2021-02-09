package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.repositories.WalletRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
		Wallet wallet = Wallet.builder().id(1L).description("Carteira").balance(BigDecimal.TEN)
				.expenses(Collections.emptyList()).build();
		given(walletRepository.findById(any(Long.class))).willThrow(ObjectNotFoundException.class);

		assertThrows(ObjectNotFoundException.class, () -> walletService.findWalletById(1L));
	}
}
