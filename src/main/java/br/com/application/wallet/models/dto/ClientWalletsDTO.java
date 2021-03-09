package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.ClientEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ClientWalletsDTO {

	@JsonProperty("client_id")
	private final Long id;

	@JsonProperty("client_name")
	private final String name;

	@JsonProperty("client_wallets")
	private final List<WalletDTO> walletDTOList;

	public ClientWalletsDTO(ClientEntity client) {
		this.id = client.getId();
		this.name = client.getName();
		this.walletDTOList = client.getWallets().stream().map(WalletDTO::new).collect(Collectors.toList());
	}

}
