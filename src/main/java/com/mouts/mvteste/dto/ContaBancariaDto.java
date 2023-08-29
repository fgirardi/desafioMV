package com.mouts.mvteste.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.mouts.mvteste.util.Ativo;
import com.mouts.mvteste.util.TipoConta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaBancariaDto {
	
	private Long idContaBancaria;
	
	
	@NotNull(message = "O saldo não pode ser nulo")
	private BigDecimal vlrSaldo;
	
	@NotNull(message = "O limite diário não pode ser nulo")
	@Positive(message = "O limite diário deve ser maior que zero")
	private BigDecimal vlrLimiteDiario;
	
	@Enumerated
	@NotNull(message = "O status não pode ser nulo")
	private Ativo flagAtivo;
	
	@Enumerated
	@NotNull(message = "O tipo da conta não pode ser nulo")
	private TipoConta tipoConta;
	
	@NotNull(message = "A pessoa não pode ser nula")
	private PessoaDto pessoaDto;
	
	private LocalDateTime dataCriacao;
	
}
