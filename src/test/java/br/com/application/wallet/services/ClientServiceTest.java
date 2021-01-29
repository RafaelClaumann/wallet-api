package br.com.application.wallet.services;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import br.com.application.wallet.models.Client;
import br.com.application.wallet.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void returnClientByIdTest() {
        long clientId = 1L;
        Client client = Client.builder().id(clientId).name("Rafael Claumann").build();
        given(clientRepository.findById(clientId)).willReturn(Optional.of(client));

        Client foundClient = clientService.findById(clientId);

        verify(clientRepository).findById(clientId);
        assertThat(foundClient).isEqualTo(client);
    }

    @Test
    void returnAllClientsTest() {
        Client client1 = Client.builder().id(1L).name("First Client").build();
        Client client2 = Client.builder().id(2L).name("Second Client").build();
        List<Client> expectedClients = Arrays.asList(client1, client2);
        given(clientRepository.findAll()).willReturn(expectedClients);

        List<Client> foundClients = clientService.findAll();

        final Long expectedId = expectedClients.get(1).getId();
        final Long foundId = foundClients.get(1).getId();

        verify(clientRepository).findAll();
        assertThat(expectedId).isEqualTo(foundId);
        assertThat(expectedClients).isEqualTo(foundClients);
    }
}
