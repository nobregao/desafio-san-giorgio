package br.com.desafio.broker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Queues {

	TOTAL("queue-payment-item-total"),
	PARCIAL("queue-payment-item-parcial"),
	EXCEDENTE("queue-payment-item-excedente");

	private final String queueName;
}
