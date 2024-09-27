package br.com.desafio.service;

import br.com.desafio.broker.Queues;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SqsService {

	private final AmazonSQS amazonSQSClient;

	@PostConstruct
	public void createQueues() {
		for (Queues queues : Queues.values()) {
			createQueue(queues.getQueueName());
		}
	}

	private void createQueue(String queueName) {
		CreateQueueResult result = amazonSQSClient.createQueue(queueName);
		System.out.printf("Queue created: %s%n", result.getQueueUrl());
	}
}