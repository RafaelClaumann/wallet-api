package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientForm {

	private String name;
	private String cpf;
	private String telephoneNumber;

	public Client convertFormToClient(ClientForm form) {
		return Client.builder().name(form.getName()).cpf(form.getCpf()).telephoneNumber(form.getTelephoneNumber())
				.build();
	}
}
