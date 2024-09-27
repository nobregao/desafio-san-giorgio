package br.com.desafio.service;

import br.com.desafio.domain.entity.Client;
import br.com.desafio.repository.ClientRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

	private final ClientRepository repository;

	public Optional<Client> findById(String id) {
		return repository.findById(id);
	}

}
