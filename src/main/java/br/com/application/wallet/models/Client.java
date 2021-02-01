package br.com.application.wallet.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
public class Client implements Serializable {
	private static final long serialVersionUID = 8607100025462907728L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String cpf;
	private String telephoneNumber;

	@OneToMany
	@JoinColumn(name = "client_id")
	private List<Wallet> wallets;

	public Client(String name, String cpf, String telephoneNumber) {
		this.name = name;
		this.cpf = cpf;
		this.telephoneNumber = telephoneNumber;
		this.wallets = new ArrayList<>();
	}

	public Client(String name, String cpf, String telephoneNumber, Wallet wallet) {
		this.name = name;
		this.cpf = cpf;
		this.telephoneNumber = telephoneNumber;
		this.wallets = new ArrayList<>(Collections.singletonList(wallet));
	}
}
