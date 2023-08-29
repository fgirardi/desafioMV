package com.mouts.mvteste.mapper;

import com.mouts.mvteste.dto.PessoaDto;
import com.mouts.mvteste.model.Pessoa;

public class PessoaMapper {

	public static Pessoa toPessoa(PessoaDto pessoaDto) {

		Pessoa pessoa = new Pessoa();
		pessoa.setCpf(pessoaDto.getCpf());
		pessoa.setDataNascimento(pessoaDto.getDataNascimento());
		pessoa.setIdPessoa(pessoaDto.getIdPessoa());
		pessoa.setNome(pessoaDto.getNome());

		return pessoa;
	}

	public static PessoaDto toPessoaDto(Pessoa pessoa) {

		PessoaDto pessoaDto = new PessoaDto();
		pessoaDto.setCpf(pessoa.getCpf());
		pessoaDto.setDataNascimento(pessoa.getDataNascimento());
		pessoaDto.setIdPessoa(pessoa.getIdPessoa());
		pessoaDto.setNome(pessoa.getNome());

		return pessoaDto;
	}

}
