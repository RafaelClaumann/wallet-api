package br.com.application.wallet.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "cpf"})
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
