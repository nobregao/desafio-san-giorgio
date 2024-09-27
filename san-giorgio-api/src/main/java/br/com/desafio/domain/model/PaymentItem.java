package br.com.desafio.domain.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentItem {

	private String id;
	private BigDecimal value;

}
