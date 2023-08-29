package com.mouts.mvteste.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.mouts.mvteste.exception.BussinessException;
import com.mouts.mvteste.model.ContaBancaria;
import com.mouts.mvteste.model.Pessoa;
import com.mouts.mvteste.model.Transacao;
import com.mouts.mvteste.repository.ContaBancariaRepository;
import com.mouts.mvteste.repository.TransacaoRepository;
import com.mouts.mvteste.util.Ativo;
import com.mouts.mvteste.util.TipoConta;
import com.mouts.mvteste.util.TipoTransacao;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

	@InjectMocks
	TransacaoService transacaoService;

	@Mock
	TransacaoRepository transacaoRepository;

	@Mock
	ContaBancariaRepository contaBancariaRepository;

	private ContaBancaria contaBancaria;
	private Transacao deposito;
	
	private Transacao saque;

	@BeforeEach
	void setup() {

		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(1L);
		pessoa.setCpf("123");
		pessoa.setNome("JOAO");
		pessoa.setDataNascimento(LocalDate.now());

		contaBancaria = new ContaBancaria();
		contaBancaria.setIdContaBancaria(1L);
		contaBancaria.setFlagAtivo(Ativo.SIM);
		contaBancaria.setTipoConta(TipoConta.CONTA_CORRENTE);
		contaBancaria.setVlrSaldo(BigDecimal.valueOf(5000));
		contaBancaria.setVlrLimiteDiario(BigDecimal.valueOf(1000));
		contaBancaria.setPessoa(pessoa);

		deposito = new Transacao();
		deposito.setContaBancaria(contaBancaria);
		deposito.setTipoTransacao(TipoTransacao.DEPOSITO);
		deposito.setVlrTransacao(BigDecimal.valueOf(5000));
			
		saque = new Transacao();
		saque.setContaBancaria(contaBancaria);
		saque.setTipoTransacao(TipoTransacao.SAQUE);
		saque.setVlrTransacao(BigDecimal.valueOf(500));
	}

	@Test
	@DisplayName("Realizar um depósito com sucesso")
	void depositarComSucesso() {
		Mockito.when(contaBancariaRepository.findById(anyLong())).thenReturn(Optional.of(contaBancaria));
		Mockito.when(transacaoRepository.save(any(Transacao.class))).thenReturn(deposito);

		// Action
		transacaoService.depositar(deposito);

		// assertions
		Mockito.verify(transacaoRepository).save(deposito);
	}

	@Test
	@DisplayName("Realizar um saque com sucesso")
	void sacarComSucesso() {
	    Mockito.when(contaBancariaRepository.findById(anyLong())).thenReturn(Optional.of(contaBancaria));
	    Mockito.when(transacaoRepository.save(any(Transacao.class))).thenReturn(saque);
	    LocalDate dataSaque = LocalDate.now();
	    Mockito.when(transacaoRepository.sumVlrTransacaoByDateAndContaBancariaAndTipoTransacao(dataSaque, 1L, TipoTransacao.SAQUE))
	    .thenReturn(BigDecimal.ZERO);

	    // Action
	    transacaoService.sacar(saque);

	    // assertions
	 	Mockito.verify(transacaoRepository).save(saque);
	}

	@Test
	@DisplayName("Tentativa de saque de uma conta inexistente")
	void sacarContaInexistente() {
	    
		Mockito.when(contaBancariaRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Action
	    BussinessException bussinessException = assertThrows(BussinessException.class, () -> transacaoService.sacar(saque));
	    // assertions
	    Assertions.assertThat(bussinessException.getMessage()).isEqualTo("Nao existe uma conta para realizar o saque");
	}

	@Test
	@DisplayName("Tentar sacar com valor zero")
	void sacarValorZero() {
		
		Mockito.when(contaBancariaRepository.findById(anyLong())).thenReturn(Optional.of(contaBancaria));
		saque.setVlrTransacao(BigDecimal.ZERO);

		// Action
	    BussinessException bussinessException = assertThrows(BussinessException.class, () -> transacaoService.sacar(saque));
	    // assertions
	    Assertions.assertThat(bussinessException.getMessage()).isEqualTo("O valor do saque deve ser maior que zero");
	}

	@Test
	@DisplayName("Tentar sacar acima do limite diário")
	void sacarAcimaLimiteDiario() {
	    
		BigDecimal limiteDiario = BigDecimal.valueOf(1000);
	    BigDecimal valorSaque = BigDecimal.valueOf(600);
	    BigDecimal somaTransacoes = BigDecimal.valueOf(700);

	    contaBancaria.setVlrLimiteDiario(limiteDiario);
	    Mockito.when(contaBancariaRepository.findById(anyLong())).thenReturn(Optional.of(contaBancaria));
	    Mockito.when(transacaoRepository.sumVlrTransacaoByDateAndContaBancariaAndTipoTransacao(any(), anyLong(), any()))
	           .thenReturn(somaTransacoes);

	    saque.setVlrTransacao(valorSaque);

	    // Action
	    BussinessException bussinessException = assertThrows(BussinessException.class, () -> transacaoService.sacar(saque));
	    // assertions
	    Assertions.assertThat(bussinessException.getMessage()).isEqualTo("Saque nao foi realizado pois atingiu o valor limite diario de saques");
	}

	@Test
	@DisplayName("Realizar um saque com sucesso em uma conta poupança")
	void sacarComSucessoContaPoupanca() {
	    BigDecimal saldoConta = BigDecimal.valueOf(600);
	    BigDecimal valorSaque = BigDecimal.valueOf(400);
	    LocalDate dataSaque = LocalDate.now();
	    Mockito.when(transacaoRepository.sumVlrTransacaoByDateAndContaBancariaAndTipoTransacao(dataSaque, 1L, TipoTransacao.SAQUE))
	    .thenReturn(BigDecimal.ZERO);

	    contaBancaria.setTipoConta(TipoConta.CONTA_POUPANCA);
	    contaBancaria.setVlrSaldo(saldoConta);
	    Mockito.when(contaBancariaRepository.findById(anyLong())).thenReturn(Optional.of(contaBancaria));
	    Mockito.when(transacaoRepository.save(any(Transacao.class))).thenReturn(saque);

	    saque.setVlrTransacao(valorSaque);

	    //Action
	    transacaoService.sacar(saque);
	    
	    // assertions
	 	Mockito.verify(transacaoRepository).save(saque);

	}

}
