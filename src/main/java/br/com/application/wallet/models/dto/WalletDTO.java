package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.Wallet;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class WalletDTO {

	@JsonProperty("wallet_id")
	private final Long id;

	@JsonProperty("wallet_description")
	private final String description;

	@JsonProperty("wallet_balance")
	private final BigDecimal balance;

	@JsonProperty("has_expenses_flag")
	private final Boolean hasExpensesFlag;

	public WalletDTO(final Wallet wallet) {
		this.id = wallet.getId();
		this.description = wallet.getDescription();
		this.balance = wallet.getBalance();
		this.hasExpensesFlag = setExpensesFlag(wallet);
	}

	private Boolean setExpensesFlag(final Wallet wallet ) {
		return Objects.nonNull(wallet.getExpenses()) && wallet.getExpenses().size() > 0;
	}
}
