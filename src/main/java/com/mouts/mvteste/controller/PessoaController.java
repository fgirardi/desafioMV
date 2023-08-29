package com.mouts.mvteste.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mouts.mvteste.dto.PessoaDto;
import com.mouts.mvteste.mapper.PessoaMapper;
import com.mouts.mvteste.model.Pessoa;
import com.mouts.mvteste.service.PessoaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pessoa")
public class PessoaController {

	private final PessoaService service;

	@PostMapping
	public ResponseEntity<PessoaDto> salvar(@Valid @RequestBody PessoaDto pessoaDto) {

		Pessoa pessoaSalva = service.salvar(PessoaMapper.toPessoa(pessoaDto));
		return ResponseEntity.status(HttpStatus.CREATED).body(PessoaMapper.toPessoaDto(pessoaSalva));

	}

	@GetMapping
	public ResponseEntity<List<PessoaDto>> buscarTodos() {
		List<Pessoa> pessoas = service.listarTodos();
		
		List<PessoaDto> pessoasDto = pessoas.stream().map(p -> PessoaMapper.toPessoaDto(p))
									.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(pessoasDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PessoaDto> buscarPorId(@PathVariable Long id) {

		Optional<Pessoa> opcPessoa = service.buscarPorId(id);
		
		if (opcPessoa.isEmpty())
			return ResponseEntity.notFound().build();

		return ResponseEntity.status(HttpStatus.OK).body(PessoaMapper.toPessoaDto(opcPessoa.get()));

	}

	@PutMapping
	public ResponseEntity<PessoaDto> alterar(@Valid @RequestBody PessoaDto pessoaDto) {
		
		//O buscarPorId vai gerar uma exception, caso nao encontrar a pessoa.
		Optional<Pessoa> opcPessoa = service.buscarPorId(pessoaDto.getIdPessoa());
		
		if (opcPessoa.isEmpty())
			return ResponseEntity.notFound().build();
		
		Pessoa pessoaSalva = service.salvar(PessoaMapper.toPessoa(pessoaDto));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(PessoaMapper.toPessoaDto(pessoaSalva));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		service.deletar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
