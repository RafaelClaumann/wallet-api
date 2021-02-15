package br.com.application.wallet.mocks;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.models.Expense;
import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.enums.ExpenseState;
import br.com.application.wallet.models.enums.ExpenseType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

public class MockClient {

	public static Client mockClientWithWalletsAndExpenses() {
		Expense expense1 = Expense.builder()
				.id(1L)
				.description("despesa1")
				.value(BigDecimal.ONE)
				.expenseType(ExpenseType.OTHER)
				.expenseState(ExpenseState.CLOSED)
				.build();
		Expense expense2 = Expense.builder()
				.id(2L)
				.description("despesa2")
				.value(BigDecimal.TEN)
				.expenseType(ExpenseType.CAR)
				.expenseState(ExpenseState.CLOSED)
				.build();

		Wallet wallet = Wallet.builder()
				.id(1L)
				.description("carteira principal")
				.balance(BigDecimal.valueOf(8000))
				.expenses(Arrays.asList(expense1, expense2))
				.build();

		Client client = Client.builder()
				.id(1L)
				.name("First Client")
				.cpf("440.966.900-15")
				.telephoneNumber("48 0 00000-0000")
				.wallets(Collections.singletonList(wallet))
				.build();

		return client;
	}
}
