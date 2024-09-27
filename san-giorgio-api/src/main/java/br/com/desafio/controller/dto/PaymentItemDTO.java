package br.com.desafio.controller.dto;

import br.com.desafio.domain.model.PaymentItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentItemDTO {

	@JsonProperty("payment_id")
	private String paymentId;

	@JsonProperty("payment_value")
	private BigDecimal paymentValue;

	private PaymentStatusEnum paymentStatus;

	public PaymentItem toPaymentItem() {
		return PaymentItem.builder()
				.id(this.paymentId)
				.value(this.paymentValue)
				.build();
	}

}
