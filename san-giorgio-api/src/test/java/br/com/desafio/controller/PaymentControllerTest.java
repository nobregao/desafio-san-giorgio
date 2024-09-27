package br.com.desafio.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.desafio.controller.dto.PaymentDTO;
import br.com.desafio.controller.dto.PaymentItemDTO;
import br.com.desafio.controller.dto.PaymentStatusEnum;
import br.com.desafio.domain.entity.Client;
import br.com.desafio.domain.entity.Payment;
import br.com.desafio.domain.model.PaymentItem;
import br.com.desafio.exception.ClientNotFoundException;
import br.com.desafio.exception.PaymentNotFoundException;
import br.com.desafio.service.ClientService;
import br.com.desafio.service.PaymentService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

	@Mock
	private ClientService clientService;

	@Mock
	private PaymentService paymentService;

	@InjectMocks
	private PaymentController paymentController;

	private PaymentDTO paymentRequest;
	private Client client;
	private Payment payment;

	@BeforeEach
	void setUp() {
		PaymentItemDTO paymentItemDTO = new PaymentItemDTO();
		paymentItemDTO.setPaymentId("1");
		paymentItemDTO.setPaymentValue(new BigDecimal("100"));

		paymentRequest = new PaymentDTO();
		paymentRequest.setClientId("1");
		paymentRequest.setPaymentItems(Collections.singletonList(paymentItemDTO));

		client = Client.builder().id("1").build();
		payment = Payment.builder().id("1").origin(new BigDecimal("100")).build();
	}

	@Test
	void testPutClientNotFound() {
		when(clientService.findById("1")).thenReturn(Optional.empty());

		assertThatExceptionOfType(ClientNotFoundException.class)
				.isThrownBy(() -> paymentController.put(paymentRequest))
				.withMessage("Client with ID 1 not found");
	}

	@Test
	void testPutPaymentNotFound() {
		when(clientService.findById("1")).thenReturn(Optional.of(client));
		when(paymentService.findById("1")).thenReturn(Optional.empty());

		assertThatExceptionOfType(PaymentNotFoundException.class)
				.isThrownBy(() -> paymentController.put(paymentRequest))
				.withMessage("Payment with ID 1 not found");
	}

	@Test
	void testPutSuccess() {
		when(clientService.findById("1")).thenReturn(Optional.of(client));
		when(paymentService.findById("1")).thenReturn(Optional.of(payment));
		when(paymentService.processPayment(any(PaymentItem.class), any(BigDecimal.class))).thenReturn(PaymentStatusEnum.TOTAL);

		ResponseEntity<PaymentDTO> response = paymentController.put(paymentRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(paymentRequest, response.getBody());
		assertEquals(PaymentStatusEnum.TOTAL, paymentRequest.getPaymentItems().get(0).getPaymentStatus());
	}
}