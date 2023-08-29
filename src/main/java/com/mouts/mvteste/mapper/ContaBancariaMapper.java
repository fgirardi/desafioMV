package com.mouts.mvteste.mapper;

import com.mouts.mvteste.dto.ContaBancariaDto;
import com.mouts.mvteste.model.ContaBancaria;

public class ContaBancariaMapper {
	
	public static ContaBancaria toContaBancaria(ContaBancariaDto contaBancariaDto) {

		ContaBancaria contaBancaria = new ContaBancaria();
		contaBancaria.setFlagAtivo(contaBancariaDto.getFlagAtivo());
		contaBancaria.setDataCriacao(contaBancariaDto.getDataCriacao());
		contaBancaria.setIdContaBancaria(contaBancariaDto.getIdContaBancaria());
		contaBancaria.setVlrLimiteDiario(contaBancariaDto.getVlrLimiteDiario());
		contaBancaria.setVlrSaldo(contaBancariaDto.getVlrSaldo());
		contaBancaria.setTipoConta(contaBancariaDto.getTipoConta());
		contaBancaria.setPessoa(PessoaMapper.toPessoa(contaBancariaDto.getPessoaDto()));
		return contaBancaria;
	}

	public static ContaBancariaDto toContaBancariaDto(ContaBancaria contaBancaria) {

		ContaBancariaDto contaBancariaDto = new ContaBancariaDto();
		contaBancariaDto.setFlagAtivo(contaBancaria.getFlagAtivo());
		contaBancariaDto.setDataCriacao(contaBancaria.getDataCriacao());
		contaBancariaDto.setIdContaBancaria(contaBancaria.getIdContaBancaria());
		contaBancariaDto.setVlrLimiteDiario(contaBancaria.getVlrLimiteDiario());
		contaBancariaDto.setVlrSaldo(contaBancaria.getVlrSaldo());
		contaBancariaDto.setTipoConta(contaBancaria.getTipoConta());
		contaBancariaDto.setPessoaDto(PessoaMapper.toPessoaDto(contaBancaria.getPessoa()));
		return contaBancariaDto;
	}

}
