package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ClientForm {

	@JsonProperty("client_name")
	@NotEmpty(message = "você deve informar um nome")
	private String name;

	@JsonProperty("client_cpf")
	@NotEmpty(message = "você deve informar um cpf" )
	@CPF
	private String cpf;

	@JsonProperty("client_telephone")
	@NotEmpty(message = "você deve informar um telefone para contato")
	@Size(min = 10, max = 15, message = "insira um DDD + Telefone")
	private String telephoneNumber;

	public Client convertFormToClient(ClientForm form) {
		return Client.builder().name(form.getName()).cpf(form.getCpf()).telephoneNumber(form.getTelephoneNumber())
				.build();
	}
}
