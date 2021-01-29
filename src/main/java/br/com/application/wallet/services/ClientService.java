package br.com.application.wallet.services;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.repositories.ClientRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findById(final Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado, id: " + id, null));
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
