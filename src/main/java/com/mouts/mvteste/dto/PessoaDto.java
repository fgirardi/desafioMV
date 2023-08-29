package com.mouts.mvteste.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDto {
	
	private Long idPessoa;
	
	@NotBlank(message = "O Nome da pessoa não pode ser vazio")
	private String nome;
	
	@NotBlank(message = "O CPF da pessoa não pode ser vazio")
	private String cpf;
	
	@NotNull(message = "A data de nascimento da pessoa não pode ser nula")
	private LocalDate dataNascimento; 

}
