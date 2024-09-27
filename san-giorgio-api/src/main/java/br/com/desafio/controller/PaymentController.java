package br.com.desafio.controller;

import br.com.desafio.controller.dto.PaymentDTO;
import br.com.desafio.controller.dto.PaymentItemDTO;
import br.com.desafio.controller.dto.PaymentStatusEnum;
import br.com.desafio.domain.entity.Client;
import br.com.desafio.domain.entity.Payment;
import br.com.desafio.exception.ClientNotFoundException;
import br.com.desafio.exception.PaymentNotFoundException;
import br.com.desafio.service.ClientService;
import br.com.desafio.service.PaymentService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

	private final ClientService clientService;
	private final PaymentService paymentService;

	@PutMapping
	public ResponseEntity<PaymentDTO> put(@RequestBody PaymentDTO paymentRequest) {

		Optional<Client> clientOptional = clientService.findById(paymentRequest.getClientId());
		if (clientOptional.isEmpty()) {
			throw new ClientNotFoundException("Client with ID " + paymentRequest.getClientId() + " not found");
		}

		for (PaymentItemDTO paymentItemDTO : paymentRequest.getPaymentItems()) {
			Optional<Payment> paymentOptional = paymentService.findById(paymentItemDTO.getPaymentId());
			if (paymentOptional.isEmpty()) {
				throw new PaymentNotFoundException("Payment with ID " + paymentItemDTO.getPaymentId() + " not found");
			} else {
				PaymentStatusEnum paymentStatusEnum = paymentService.processPayment(paymentItemDTO.toPaymentItem(), paymentOptional.get().getOrigin());
				paymentItemDTO.setPaymentStatus(paymentStatusEnum);
			}
		}

		return ResponseEntity.status(HttpStatus.OK).body(paymentRequest);
	}
}
