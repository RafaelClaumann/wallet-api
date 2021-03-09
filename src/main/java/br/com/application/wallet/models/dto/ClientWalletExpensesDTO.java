package br.com.application.wallet.models.dto;

import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.models.ClientEntity;
import br.com.application.wallet.models.WalletEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ClientWalletExpensesDTO {

	@JsonProperty("client_id")
	private final Long clientId;

	@JsonProperty("client_name")
	private final String clientName;

	@JsonProperty("wallet_id")
	private final Long walletId;

	@JsonProperty("wallet_description")
	private final String walletDescription;

	@JsonProperty("wallet_expenses")
	private final List<ExpenseDTO> expensesDtoList;

	public ClientWalletExpensesDTO(final ClientEntity client, final Long walletId) {
		this.clientId = client.getId();
		this.clientName = client.getName();
		this.walletId = walletId;

		final WalletEntity wallet = getClientWalletById(client, walletId);
		this.walletDescription = wallet.getDescription();
		this.expensesDtoList = turnWalletExpensesListToExpensesDTOList(wallet);
	}

	private WalletEntity getClientWalletById(final ClientEntity client, final Long walletId) {
		final Optional<WalletEntity> first = client.getWallets().stream().filter(wallet1 -> wallet1.getId().equals(walletId))
				.findFirst();
		return first.orElseThrow(() -> new WalletNotFoundException("Carteira não encontrada, id: " + walletId));
	}

	private List<ExpenseDTO> turnWalletExpensesListToExpensesDTOList(final WalletEntity wallet) {
		return wallet.getExpenses().stream().map(ExpenseDTO::new).collect(Collectors.toList());
	}

}
