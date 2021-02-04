package br.com.application.wallet.repositories;

import br.com.application.wallet.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	public Optional<Client> findClientByCpf(String cpf);
}
