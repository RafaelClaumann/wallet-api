package br.com.application.wallet.models.dto.form;

import br.com.application.wallet.models.ClientEntity;
import br.com.application.wallet.models.api.ErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ClientForm {

	@JsonProperty("client_name")
	@NotEmpty(message = ErrorMessage.NOT_NULL_FIELD)
	private String name;

	@JsonProperty("client_cpf")
	@NotEmpty(message = ErrorMessage.NOT_NULL_FIELD)
	@CPF
	private String cpf;

	@JsonProperty("client_telephone")
	@NotEmpty(message = ErrorMessage.NOT_NULL_FIELD)
	@Size(min = 10, max = 15, message = "insira um DDD + Telefone")
	private String telephoneNumber;

	public ClientEntity convertFormToClient(ClientForm form) {
		return ClientEntity.builder().name(form.getName()).cpf(form.getCpf()).telephoneNumber(form.getTelephoneNumber())
				.build();
	}
}
