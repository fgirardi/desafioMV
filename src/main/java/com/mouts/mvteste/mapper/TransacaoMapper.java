package com.mouts.mvteste.mapper;


import com.mouts.mvteste.dto.TransacaoDto;
import com.mouts.mvteste.model.Transacao;

public class TransacaoMapper {
	
	public static Transacao toTransacao(TransacaoDto transacaoDto) {

		Transacao transacao = new Transacao();
		transacao.setContaBancaria(ContaBancariaMapper.toContaBancaria(transacaoDto.getContaBancaria()));
		transacao.setDataTransacao(transacaoDto.getDataTransacao());
		transacao.setIdTransacao(transacaoDto.getIdTransacao());
		transacao.setVlrTransacao(transacaoDto.getVlrTransacao());
		transacao.setTipoTransacao(transacaoDto.getTipoTransacao());
		return transacao;
	}

	public static TransacaoDto toTransacaoDto(Transacao transacao) {

		TransacaoDto transacaoDto = new TransacaoDto();
		transacaoDto.setContaBancaria(ContaBancariaMapper.toContaBancariaDto(transacao.getContaBancaria()));
		transacaoDto.setDataTransacao(transacao.getDataTransacao());
		transacaoDto.setIdTransacao(transacao.getIdTransacao());
		transacaoDto.setVlrTransacao(transacao.getVlrTransacao());
		transacaoDto.setTipoTransacao(transacao.getTipoTransacao());
		return transacaoDto;
	}

}
