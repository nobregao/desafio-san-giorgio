package br.com.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.desafio.broker.Publisher;
import br.com.desafio.controller.dto.PaymentStatusEnum;
import br.com.desafio.domain.entity.Payment;
import br.com.desafio.domain.model.PaymentItem;
import br.com.desafio.repository.PaymentRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private Publisher publisher;

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private PaymentService paymentService;

	@Test
	void testFindById() {
		String paymentId = "123";
		Payment payment = Payment.builder().id(paymentId).build();

		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

		Optional<Payment> result = paymentService.findById(paymentId);

		assertEquals(Optional.of(payment), result);
	}

	@Test
	void testProcessPaymentPartial() {
		PaymentItem paymentItem = PaymentItem.builder().value(new BigDecimal("50")).build();
		BigDecimal valueDatabase = new BigDecimal("100");

		PaymentStatusEnum result = paymentService.processPayment(paymentItem, valueDatabase);

		assertEquals(PaymentStatusEnum.PARCIAL, result);
		verify(publisher).sendMessageParcial(paymentItem);
		verify(publisher, never()).sendMessageTotal(paymentItem);
		verify(publisher, never()).sendMessageExcedente(paymentItem);
	}

	@Test
	void testProcessPaymentTotal() {
		PaymentItem paymentItem = PaymentItem.builder().value(new BigDecimal("100")).build();
		BigDecimal valueDatabase = new BigDecimal("100");

		PaymentStatusEnum result = paymentService.processPayment(paymentItem, valueDatabase);

		assertEquals(PaymentStatusEnum.TOTAL, result);
		verify(publisher).sendMessageTotal(paymentItem);
		verify(publisher, never()).sendMessageParcial(paymentItem);
		verify(publisher, never()).sendMessageExcedente(paymentItem);
	}

	@Test
	void testProcessPaymentExcedente() {
		PaymentItem paymentItem = PaymentItem.builder().value(new BigDecimal("150")).build();
		BigDecimal valueDatabase = new BigDecimal("100");

		PaymentStatusEnum result = paymentService.processPayment(paymentItem, valueDatabase);

		assertEquals(PaymentStatusEnum.EXCEDENTE, result);
		verify(publisher).sendMessageExcedente(paymentItem);
		verify(publisher, never()).sendMessageTotal(paymentItem);
		verify(publisher, never()).sendMessageParcial(paymentItem);
	}
}