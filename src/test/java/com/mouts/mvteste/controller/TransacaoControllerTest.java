package com.mouts.mvteste.controller;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mouts.mvteste.dto.ContaBancariaDto;
import com.mouts.mvteste.dto.PessoaDto;
import com.mouts.mvteste.dto.TransacaoDto;
import com.mouts.mvteste.mapper.TransacaoMapper;
import com.mouts.mvteste.model.Transacao;
import com.mouts.mvteste.service.TransacaoService;
import com.mouts.mvteste.util.Ativo;
import com.mouts.mvteste.util.TipoConta;
import com.mouts.mvteste.util.TipoTransacao;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class TransacaoControllerTest {

	private PessoaDto pessoaDto;
	private ContaBancariaDto contaBancariaDto;
	private TransacaoDto transacaoDto;
	
	private Transacao transacao;
	private Transacao transacaoSalva;
	
	@Mock
	TransacaoService transacaoService;
	
	@InjectMocks
	TransacaoController transacaoController;
	
	@BeforeEach
	void setup() {
		
		pessoaDto = new PessoaDto();
		pessoaDto.setCpf("123");
		pessoaDto.setIdPessoa(1L);
		pessoaDto.setNome("JOAO");
		pessoaDto.setDataNascimento(LocalDate.now());
			
		contaBancariaDto = new ContaBancariaDto();  
		contaBancariaDto.setDataCriacao(LocalDateTime.now());
		contaBancariaDto.setFlagAtivo(Ativo.SIM);
		contaBancariaDto.setIdContaBancaria(1L);
		contaBancariaDto.setPessoaDto(pessoaDto);
		contaBancariaDto.setTipoConta(TipoConta.CONTA_CORRENTE);
		contaBancariaDto.setVlrLimiteDiario(BigDecimal.valueOf(1000));
		contaBancariaDto.setVlrSaldo(BigDecimal.valueOf(5000));
		
		transacaoDto = new TransacaoDto();
		transacaoDto.setContaBancaria(contaBancariaDto);
		transacaoDto.setIdTransacao(1L);
		transacaoDto.setTipoTransacao(TipoTransacao.SAQUE);
		transacaoDto.setVlrTransacao(BigDecimal.valueOf(5000));

	    transacao = TransacaoMapper.toTransacao(transacaoDto);
	    
	    transacaoSalva = TransacaoMapper.toTransacao(transacaoDto);
	}
	
	@Test
	@DisplayName("Realizar um depósito com sucesso")
	void depositarComSucesso() {
	    
	    Mockito.when(transacaoService.depositar(any(Transacao.class))).thenReturn(transacaoSalva);

	    ResponseEntity<TransacaoDto> response = transacaoController.depositar(transacaoDto);

	    assertEquals(HttpStatus.CREATED, response.getStatusCode());
	    assertEquals(transacaoSalva, TransacaoMapper.toTransacao(response.getBody()));
	}

	@Test
	@DisplayName("Realizar um saque com sucesso")
	void sacarComSucesso() {

	    Mockito.when(transacaoService.sacar(any(Transacao.class))).thenReturn(transacaoSalva);

	    ResponseEntity<TransacaoDto> response = transacaoController.sacar(transacaoDto);

	    assertEquals(HttpStatus.CREATED, response.getStatusCode());
	    assertEquals(transacaoSalva, TransacaoMapper.toTransacao(response.getBody()));
	}
	
	@Test
	@DisplayName("Obter extrato por CPF e período")
	void getTransacoesByDateRangeAndCpf() {
	    LocalDate dataInicio = LocalDate.now();
	    LocalDate dataFim = LocalDate.now();
	    String cpf = "123";
	    
	    List<Transacao> transacaoList = new ArrayList<Transacao>();
	    transacaoList.add(transacao);

	    List<TransacaoDto> transacaoDtoList = transacaoList.stream()
	            .map(TransacaoMapper::toTransacaoDto)
	            .collect(Collectors.toList());

	    Mockito.when(transacaoService.getExtratoByCpfAndDataTransacao(cpf, dataInicio, dataFim))
	           .thenReturn(transacaoList);

	    ResponseEntity<List<TransacaoDto>> response = transacaoController.getTransacoesByDateRangeAndCpf(dataInicio, dataFim, cpf);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(transacaoDtoList, response.getBody());
	}

	@Test
	@DisplayName("Obter extrato por ID pessoa e período")
	void getTransacoesByDateRangeAndIdPessoa() {
	    LocalDate dataInicio = LocalDate.now();
	    LocalDate dataFim = LocalDate.now();
	    Long idPessoa = 1L;
	    List<Transacao> transacaoList = new ArrayList<Transacao>();
	    transacaoList.add(transacao);
	    transacaoList.add(transacao);
	    transacaoList.add(transacao);
	    
	    List<TransacaoDto> transacaoDtoList = transacaoList.stream()
	            .map(TransacaoMapper::toTransacaoDto)
	            .collect(Collectors.toList());

	    Mockito.when(transacaoService.getExtratoByIdPessoaAndDataTransacao(idPessoa, dataInicio, dataFim))
	           .thenReturn(transacaoList);

	    ResponseEntity<List<TransacaoDto>> response = transacaoController.getTransacoesByDateRangeAndCpf(dataInicio, dataFim, idPessoa);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(transacaoDtoList, response.getBody());
	}
	
}
