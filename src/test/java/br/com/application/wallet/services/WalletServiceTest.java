package br.com.application.wallet.services;

import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
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

import static br.com.application.wallet.mocks.MockExpense.mockTwoClosedExpensesList;
import static br.com.application.wallet.mocks.MockExpense.mockTwoOpenedExpensesList;
import static br.com.application.wallet.mocks.MockWallet.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepositoryMock;

    @Mock
    private ClientService clientServiceMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnWalletByIdTest() {
        Wallet wallet = mockSingleWallet(1L, Collections.emptyList());

        given(walletRepositoryMock.findById(any(Long.class))).willReturn(Optional.of(wallet));

        final Wallet foundWallet = walletService.findWalletById(1L);

        assertThat(foundWallet).isEqualTo(wallet);
    }

    @Test
    void shouldThrowAnExceptionWhenWalletNotFoundByIdTest() {
        given(walletRepositoryMock.findById(any(Long.class))).willReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.findWalletById(1L));
    }

    @Test
    void shouldReturnAListOfWalletsTest() {
        Wallet wallet1 = mockSingleWalletWithOpenedExpenses(1L);
        Wallet wallet2 = mockSingleWalletWithClosedExpenses(2L);
        final List<Wallet> wallets = Arrays.asList(wallet1, wallet2);

        given(walletRepositoryMock.findAll()).willReturn(wallets);

        final List<Wallet> allWallets = walletService.findAllWallets();

        assertThat(allWallets).isEqualTo(wallets);
    }

    @Test
    void shouldReturnAnEmptyListOfWalletsTest() {
        given(walletRepositoryMock.findAll()).willReturn(Collections.emptyList());

        final List<Wallet> allWallets = walletService.findAllWallets();

        assertThat(allWallets).isEmpty();
    }

    @Test
    void shouldSaveAWalletTest() {
        Wallet wallet = mockSingleWallet(1L);
        Client client = Client.builder().id(1L).name("First Client").cpf("531.521.400-10")
                .telephoneNumber("48 0 0000-0000").build();

        given(clientServiceMock.findClientById(1L)).willReturn(client);
        given(walletRepositoryMock.save(any(Wallet.class))).willReturn(wallet);

        final Wallet savedWallet = walletService.saveWallet(1L, wallet);

        verify(walletRepositoryMock).save(wallet);
        assertThat(wallet).isEqualTo(savedWallet);
    }

    @Test
    void shouldCreateAnEmptyListOfExpensesForWalletTest() {
        final Wallet walletMock = mockSingleWallet(1L);
        walletMock.setExpenses(null);

        given(walletRepositoryMock.findById(any(Long.class))).willReturn(Optional.of(walletMock));

        final Wallet wallet = walletService.findWalletById(1L);

        assertThat(wallet.getExpenses()).isNotNull();
        assertThat(wallet.getExpenses()).isEmpty();
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

        given(clientServiceMock.findClientById(1L)).willReturn(client);
        given(walletRepositoryMock.save(any(Wallet.class))).willReturn(wallet);

        final Wallet savedWallet = walletService.saveWallet(1L, wallet);

        assertThat(wallet).isEqualTo(savedWallet);
        assertThat(client.getWallets()).isNotNull();
        assertThat(client.getWallets()).containsOnly(wallet);
    }

    @Test
    void shouldThrowExceptionForDuplicateWalletSaveTest() {
        final List<Expense> expenses = mockTwoClosedExpensesList();
        final Wallet wallet = mockSingleWalletWithExpenses(1L, expenses);
        final Client client = Client.builder().id(1L).name("First Client").cpf("531.521.400-10")
                .telephoneNumber("48 0 0000-0000").wallets(Collections.singletonList(wallet)).build();

        given(clientServiceMock.findClientById(any(Long.class))).willReturn(client);

        assertThrows(IllegalArgumentException.class, () -> walletService.saveWallet(1L, wallet));
    }

    @Test
    void shouldDeleteWalletWithClosedExpensesTest() {
        final List<Expense> expenses = mockTwoClosedExpensesList();
        final Wallet wallet = mockSingleWalletWithExpenses(1L, expenses);

        given(walletRepositoryMock.findById(1L)).willReturn(Optional.of(wallet));

        final boolean wasDeleted = walletService.deleteWallet(1L);

        assertTrue(wasDeleted);
    }

    @Test
    void shouldThrowExceptionWhenDeleteWalletWithOpenedExpensesTest() {
        final List<Expense> expenses = mockTwoOpenedExpensesList();
        final Wallet wallet = mockSingleWalletWithExpenses(1L, expenses);

        given(walletRepositoryMock.findById(1L)).willReturn(Optional.of(wallet));

        assertThrows(OpenedExpensesException.class, () -> walletService.deleteWallet(1L));
    }
}
