package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.Wallet;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class WalletDTO {

	@JsonProperty("wallet_id")
	private final Long id;

	@JsonProperty("wallet_description")
	private final String description;

	@JsonProperty("wallet_balance")
	private final BigDecimal balance;

	public WalletDTO(Wallet wallet) {
		this.id = wallet.getId();
		this.description = wallet.getDescription();
		this.balance = wallet.getBalance();
	}

}
