package br.com.application.wallet.models.dto;

import br.com.application.wallet.models.ClientEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
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

    public ClientDTO(ClientEntity client) {
        this.id = client.getId();
        this.name = client.getName();
        this.cpf = client.getCpf();
        this.telephoneNumber = client.getTelephoneNumber();
    }

    public static List<ClientDTO> convertListToDTO(final List<ClientEntity> clientList) {
        return clientList.stream().map(ClientDTO::new).collect(Collectors.toList());
    }

}
