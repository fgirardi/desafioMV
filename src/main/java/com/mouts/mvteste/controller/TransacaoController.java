package com.mouts.mvteste.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mouts.mvteste.dto.TransacaoDto;
import com.mouts.mvteste.mapper.TransacaoMapper;
import com.mouts.mvteste.model.Transacao;
import com.mouts.mvteste.service.TransacaoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transacao")
public class TransacaoController {

	private final TransacaoService service;

	@PostMapping("/depositar")
	public ResponseEntity<TransacaoDto> depositar(@Valid @RequestBody TransacaoDto transacaoDto) {

		Transacao TransacaoSalva = service.depositar(TransacaoMapper.toTransacao(transacaoDto));
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(TransacaoMapper.toTransacaoDto(TransacaoSalva));

	}
	
	@PostMapping("/sacar")
	public ResponseEntity<TransacaoDto> sacar(@Valid @RequestBody TransacaoDto transacaoDto) {

		Transacao TransacaoSalva = service.sacar(TransacaoMapper.toTransacao(transacaoDto));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(TransacaoMapper.toTransacaoDto(TransacaoSalva));

	}

	@GetMapping("/extratoPorCpf")
	public ResponseEntity<List<TransacaoDto>> getTransacoesByDateRangeAndCpf(@RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
																			 @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim, 
																			 @RequestParam String cpf) {

		List<Transacao> transacaoList = service.getExtratoByCpfAndDataTransacao(cpf, dataInicio, dataFim);
			
		List<TransacaoDto> transacaoListDto = transacaoList.stream().map(t -> TransacaoMapper.toTransacaoDto(t)).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(transacaoListDto);
	}
	
	@GetMapping("/extratoPorIdPessoa")
	public ResponseEntity<List<TransacaoDto>> getTransacoesByDateRangeAndCpf(@RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
			 																 @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
			 																 @RequestParam Long idPessoa) {

		
		List<Transacao> transacaoList = service.getExtratoByIdPessoaAndDataTransacao(idPessoa, dataInicio, dataFim); 
		List<TransacaoDto> transacaoListDto = transacaoList.stream().map(t -> TransacaoMapper.toTransacaoDto(t)).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(transacaoListDto);
	}
}
