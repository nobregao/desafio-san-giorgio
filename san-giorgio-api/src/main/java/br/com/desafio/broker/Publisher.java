package br.com.desafio.broker;

import static br.com.desafio.broker.Queues.EXCEDENTE;
import static br.com.desafio.broker.Queues.PARCIAL;
import static br.com.desafio.broker.Queues.TOTAL;

import br.com.desafio.domain.model.PaymentItem;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Publisher {

	private final AmazonSQS amazonSQSClient;
	private final ObjectMapper objectMapper;

	public void sendMessageTotal(PaymentItem paymentItem) {
		sendMessage(TOTAL.getQueueName(), paymentItem);
	}

	public void sendMessageParcial(PaymentItem paymentItem) {
		sendMessage(PARCIAL.getQueueName(), paymentItem);
	}

	public void sendMessageExcedente(PaymentItem paymentItem) {
		sendMessage(EXCEDENTE.getQueueName(), paymentItem);
	}

	private void sendMessage(String queueName, PaymentItem paymentItem) {
		GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(queueName);

		try {
			SendMessageResult result = amazonSQSClient.sendMessage(queueUrl.getQueueUrl(), objectMapper.writeValueAsString(paymentItem));
			System.out.printf("Message sent to %s with ID: %s%n", queueName, result.getMessageId());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
