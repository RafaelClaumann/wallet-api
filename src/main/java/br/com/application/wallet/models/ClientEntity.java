package br.com.application.wallet.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLIENT")
public class ClientEntity implements Serializable {
	private static final long serialVersionUID = 8607100025462907728L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@Column(unique = true)
	private String cpf;
	private String telephoneNumber;

	@OneToMany
	@JoinColumn(name = "client_id")
	private List<WalletEntity> wallets = new ArrayList<>();

	public ClientEntity(String name, String cpf, String telephoneNumber) {
		this.name = name;
		this.cpf = cpf;
		this.telephoneNumber = telephoneNumber;
		this.wallets = new ArrayList<>();
	}

	public ClientEntity(String name, String cpf, String telephoneNumber, WalletEntity wallet) {
		this.name = name;
		this.cpf = cpf;
		this.telephoneNumber = telephoneNumber;
		this.wallets = Arrays.asList(wallet);
	}
}
