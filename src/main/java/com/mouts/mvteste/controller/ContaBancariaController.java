package com.mouts.mvteste.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mouts.mvteste.dto.ContaBancariaDto;
import com.mouts.mvteste.mapper.ContaBancariaMapper;
import com.mouts.mvteste.model.ContaBancaria;
import com.mouts.mvteste.service.ContaBancariaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contaBancaria")
public class ContaBancariaController {

	private final ContaBancariaService service;

	// * Implementar path que realiza a criação de uma conta;
	@PostMapping
	public ResponseEntity<ContaBancariaDto> criarConta(@Valid @RequestBody ContaBancariaDto ContaBancariaDto) {

		ContaBancaria ContaBancariaSalva = service.criarConta(ContaBancariaMapper.toContaBancaria(ContaBancariaDto));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ContaBancariaMapper.toContaBancariaDto(ContaBancariaSalva));

	}

	// * Implementar path que realiza operação de consulta de saldo em determinada conta;
	@GetMapping("/{id}")
	public ResponseEntity<ContaBancariaDto> consultaSaldoPorId(@PathVariable Long id) {

		Optional<ContaBancaria> opcContaBancaria = service.buscarPorId(id);

		if (opcContaBancaria.isEmpty())
			return ResponseEntity.notFound().build();

		return ResponseEntity.status(HttpStatus.OK)
				.body(ContaBancariaMapper.toContaBancariaDto(opcContaBancaria.get()));

	}

	// * Implementar path que realiza o bloqueio de uma conta;
	@PutMapping("/bloquear/{id}")
	public ResponseEntity<ContaBancariaDto> bloquear(@PathVariable Long id) {
		ContaBancaria contaBancaria = service.bloquearPorId(id);
		ContaBancariaDto contaBancariaDto = ContaBancariaMapper.toContaBancariaDto(contaBancaria);
		return ResponseEntity.status(HttpStatus.OK).body(contaBancariaDto);
	}
		

	// * Implementar path que realiza o desbloqueio de uma conta;
	@PutMapping("/desbloquear/{id}")
	public ResponseEntity<ContaBancariaDto> desbloquear(@PathVariable Long id) {
		ContaBancaria contaBancaria = service.desbloquearPorId(id);
		ContaBancariaDto contaBancariaDto = ContaBancariaMapper.toContaBancariaDto(contaBancaria);
		return ResponseEntity.status(HttpStatus.OK).body(contaBancariaDto);
	}

	
}
