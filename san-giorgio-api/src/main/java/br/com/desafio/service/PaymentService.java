package br.com.desafio.service;

import br.com.desafio.broker.Publisher;
import br.com.desafio.controller.dto.PaymentStatusEnum;
import br.com.desafio.domain.entity.Payment;
import br.com.desafio.domain.model.PaymentItem;
import br.com.desafio.repository.PaymentRepository;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository repository;
	private final Publisher publisher;

	public Optional<Payment> findById(String id) {
		return repository.findById(id);
	}

	public PaymentStatusEnum processPayment(PaymentItem paymentItem, BigDecimal valueDatabase) {
		var valueResponse = paymentItem.getValue();

		if (valueResponse.doubleValue() < valueDatabase.doubleValue()) {
			publisher.sendMessageParcial(paymentItem);
			return PaymentStatusEnum.PARCIAL;
		} else if (valueResponse.doubleValue() == valueDatabase.doubleValue()) {
			publisher.sendMessageTotal(paymentItem);
			return PaymentStatusEnum.TOTAL;
		} else {
			publisher.sendMessageExcedente(paymentItem);
			return PaymentStatusEnum.EXCEDENTE;
		}
	}

}
