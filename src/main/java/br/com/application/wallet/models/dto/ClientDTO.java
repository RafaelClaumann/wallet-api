package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

	@JsonProperty("client_id")
	private Long id;

	@JsonProperty("client_name")
	private String name;

	@JsonProperty("client_cpf")
	private String cpf;

	@JsonProperty("client_telephone")
	private String telephoneNumber;

	public ClientDTO(Client client) {
		this.id = client.getId();
		this.name = client.getName();
		this.cpf = client.getCpf();
		this.telephoneNumber = client.getTelephoneNumber();
	}
	
}
