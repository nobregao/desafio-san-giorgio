package br.com.desafio.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

	@JsonProperty("client_id")
	private String clientId;

	@JsonProperty("payment_items")
	private List<PaymentItemDTO> paymentItems;
}
