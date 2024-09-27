package br.com.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.com.desafio.domain.entity.Client;
import br.com.desafio.repository.ClientRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private ClientService clientService;

	@Test
	void testFindById() {
		String clientId = "123";
		Client client = Client.builder().id(clientId).build();

		when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

		Optional<Client> result = clientService.findById(clientId);

		assertEquals(Optional.of(client), result);
	}

}