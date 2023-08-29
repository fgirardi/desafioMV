package com.mouts.mvteste.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

import com.mouts.mvteste.exception.BussinessException;
import com.mouts.mvteste.model.ContaBancaria;
import com.mouts.mvteste.model.Pessoa;
import com.mouts.mvteste.repository.ContaBancariaRepository;
import com.mouts.mvteste.repository.PessoaRepository;
import com.mouts.mvteste.util.Ativo;
import com.mouts.mvteste.util.TipoConta;

@SpringBootTest
@AutoConfigureMockMvc
public class ContaBancariaServiceTest {
	
	@InjectMocks
	ContaBancariaService contaBancariaService;
	
	@Mock
	ContaBancariaRepository contaBancariaRepository;
	
	@Mock
	PessoaRepository pessoaRepository;
	
	@Captor
	ArgumentCaptor<ContaBancaria> contaBancariaCaptor;
	
	@Test
	@DisplayName("Criar uma conta com sucesso")	
	void criarContaComSucesso() {
		// Criação de dados fictícios
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(1L);
		pessoa.setCpf("123");
		pessoa.setNome("JOAO");
		pessoa.setDataNascimento(LocalDate.now());
		
		ContaBancaria contaBancaria = new ContaBancaria();
		contaBancaria.setFlagAtivo(Ativo.SIM);
		contaBancaria.setPessoa(pessoa);
		contaBancaria.setTipoConta(TipoConta.CONTA_CORRENTE);
		contaBancaria.setVlrLimiteDiario(new BigDecimal("1000"));
		contaBancaria.setVlrSaldo(new BigDecimal("1000"));
		
		// Configuração do mock do repositório PessoaRepository
		Mockito.when(pessoaRepository.findByIdPessoa(1L)).thenReturn(Optional.of(pessoa));
		
		// Configuração do mock do repositório ContaBancariaRepository
		Mockito.when(contaBancariaRepository.findAllContaBancariaByPessoaId(anyLong())).thenReturn(new ArrayList<ContaBancaria>());
		
		// Action
		contaBancariaService.criarConta(contaBancaria);
	    
		// assertions
		Mockito.verify(pessoaRepository).findByIdPessoa(1L);
		Mockito.verify(contaBancariaRepository).findAllContaBancariaByPessoaId(anyLong());
		Mockito.verify(contaBancariaRepository).save(contaBancariaCaptor.capture());
		contaBancaria = contaBancariaCaptor.getValue();
		//Comentado pois a data de criacao foi preenchida na Jpa.
		//Assertions.assertThat(contaBancaria.getDataCriacao()).isNotNull();
	}
	
	@Test
	@DisplayName("Criar uma conta onde que a pessoa ainda nao foi criada")	
	void criarContaSemPessoaPreCriada() {
		// Criação de dados fictícios
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(1L);
		pessoa.setCpf("123");
		pessoa.setNome("JOAO");
		pessoa.setDataNascimento(LocalDate.now());
		
		ContaBancaria contaBancaria = new ContaBancaria();
		contaBancaria.setFlagAtivo(Ativo.SIM);
		contaBancaria.setPessoa(pessoa);
		contaBancaria.setTipoConta(TipoConta.CONTA_CORRENTE);
		contaBancaria.setVlrLimiteDiario(new BigDecimal("1000"));
		contaBancaria.setVlrSaldo(new BigDecimal("1000"));
		
		// Configuração do mock do repositório PessoaRepository
		Mockito.when(pessoaRepository.findByIdPessoa(1L)).thenReturn(Optional.empty());
		
		// Action
		BussinessException bussinessException = assertThrows(BussinessException.class, () -> {
			contaBancariaService.criarConta(contaBancaria);
		}); 
		
		// assertions
		Assertions.assertThat(bussinessException.getMessage()).isEqualTo("Nao é possivel criar a conta pois a pessoa nao existe");
	}
	
	@Test
	@DisplayName("Criar uma conta Inativa.")	
	void criarContaInativaComErro() {
		// Criação de dados fictícios
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(1L);
		pessoa.setCpf("123");
		pessoa.setNome("JOAO");
		pessoa.setDataNascimento(LocalDate.now());
		
		ContaBancaria contaBancaria = new ContaBancaria();
		contaBancaria.setFlagAtivo(Ativo.NAO);
		contaBancaria.setPessoa(pessoa);
		contaBancaria.setTipoConta(TipoConta.CONTA_CORRENTE);
		contaBancaria.setVlrLimiteDiario(new BigDecimal("1000"));
		contaBancaria.setVlrSaldo(new BigDecimal("1000"));
		
		// Action
		BussinessException bussinessException = assertThrows(BussinessException.class, () -> {
			contaBancariaService.criarConta(contaBancaria);
		}); 
				
		// assertions
		Assertions.assertThat(bussinessException.getMessage()).isEqualTo("Nao é permitido criar uma conta bancaria INATIVA");
		
		
	}

}
