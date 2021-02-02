package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

	private String name;
	private String cpf;
	private String telephoneNumber;

	public ClientDTO(Client client) {
		this.name = client.getName();
		this.cpf = client.getCpf();
		this.telephoneNumber = client.getTelephoneNumber();
	}
	
}
