package br.com.desafio.broker;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.desafio.domain.model.PaymentItem;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PublisherTest {

	@Mock
	private AmazonSQS amazonSQSClient;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private Publisher publisher;

	private PaymentItem paymentItem;

	@BeforeEach
	void setUp() {
		paymentItem = PaymentItem.builder().id("1").value(new BigDecimal("100")).build();
		GetQueueUrlResult queueUrlResult = new GetQueueUrlResult().withQueueUrl("http://queue-url");
		SendMessageResult sendMessageResult = new SendMessageResult().withMessageId("msg-id");

		when(amazonSQSClient.getQueueUrl(anyString())).thenReturn(queueUrlResult);
		when(amazonSQSClient.sendMessage(anyString(), anyString())).thenReturn(sendMessageResult);
	}

	@Test
	void testSendMessageTotal() throws Exception {
		when(objectMapper.writeValueAsString(paymentItem)).thenReturn("message-payment-total");

		publisher.sendMessageTotal(paymentItem);

		verify(amazonSQSClient).sendMessage("http://queue-url", "message-payment-total");
	}

	@Test
	void testSendMessageParcial() throws Exception {
		when(objectMapper.writeValueAsString(paymentItem)).thenReturn("message-payment-parcial");

		publisher.sendMessageParcial(paymentItem);

		verify(amazonSQSClient).sendMessage("http://queue-url", "message-payment-parcial");
	}

	@Test
	void testSendMessageExcedente() throws Exception {
		when(objectMapper.writeValueAsString(paymentItem)).thenReturn("message-payment-excedente");

		publisher.sendMessageExcedente(paymentItem);

		verify(amazonSQSClient).sendMessage("http://queue-url", "message-payment-excedente");
	}
}