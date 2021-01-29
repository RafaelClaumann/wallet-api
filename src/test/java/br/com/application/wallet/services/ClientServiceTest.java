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
        Client client = Client.builder().id(clientId).name("Rafael Claumann").cpf("000000000-00").build();
        given(clientRepository.findById(clientId)).willReturn(Optional.of(client));

        Client foundClient = clientService.findById(1L);

        verify(clientRepository).findById(clientId);
        assertThat(foundClient).isEqualTo(client);
    }
}
