package com.mouts.mvteste.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.mouts.mvteste.util.TipoTransacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDto {
	
	private Long idTransacao;

	@NotNull(message = "A conta bancaria nao pode ser nula")
	private ContaBancariaDto contaBancaria;
	
	@NotNull(message = "O valor da transacaonão pode ser nulo")
	private BigDecimal vlrTransacao;
	
	@NotNull(message = "O Tipo de transacao nao pode ser nulo")
	private TipoTransacao tipoTransacao;

	private LocalDateTime dataTransacao;
	
}
