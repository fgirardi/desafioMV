package com.mouts.mvteste.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.mouts.mvteste.dto.ContaBancariaDto;
import com.mouts.mvteste.dto.PessoaDto;
import com.mouts.mvteste.mapper.ContaBancariaMapper;
import com.mouts.mvteste.model.ContaBancaria;
import com.mouts.mvteste.service.ContaBancariaService;
import com.mouts.mvteste.util.Ativo;
import com.mouts.mvteste.util.TipoConta;

@DisplayName("ContaBancariaController Teste")
@ExtendWith(MockitoExtension.class)
public class ContaBancariaControllerTest {

	@InjectMocks
    private ContaBancariaController controller;
    
	@Mock
    private ContaBancariaService service;
	
	private PessoaDto pessoaDto;
	private ContaBancariaDto contaBancariaDto;
	private ContaBancaria contaBancaria;

    @BeforeEach
    void setUp() {
    	
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

		contaBancaria = ContaBancariaMapper.toContaBancaria(contaBancariaDto);
	   
    }

    @Test
    @DisplayName("Criar uma conta bancária com sucesso")
    void criarContaComSucesso() {
       
        when(service.criarConta(contaBancaria)).thenReturn(contaBancaria);
        ResponseEntity<ContaBancariaDto> response = controller.criarConta(contaBancariaDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    @DisplayName("Consultar saldo por ID")
    void consultarSaldoPorId() {
        Long idContaBancaria = 1L;
        when(service.buscarPorId(idContaBancaria)).thenReturn(Optional.of(contaBancaria));
        ResponseEntity<ContaBancariaDto> response = controller.consultaSaldoPorId(idContaBancaria);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @DisplayName("Efetuar o bloqueio de uma conta bancária")
    void bloquearConta() {
        when(service.bloquearPorId(1L)).thenReturn(contaBancaria);
        ResponseEntity<ContaBancariaDto> response = controller.bloquear(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
    }

    @Test
    @DisplayName("Desbloquear uma conta bancária")
    void desbloquearConta() {
        Long idContaBancaria = 1L;
        when(service.desbloquearPorId(idContaBancaria)).thenReturn(contaBancaria);
        ResponseEntity<ContaBancariaDto> response = controller.desbloquear(idContaBancaria);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
}
