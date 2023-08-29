package com.mouts.mvteste.service;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mouts.mvteste.exception.BussinessException;
import com.mouts.mvteste.model.ContaBancaria;
import com.mouts.mvteste.model.Transacao;
import com.mouts.mvteste.repository.ContaBancariaRepository;
import com.mouts.mvteste.repository.TransacaoRepository;
import com.mouts.mvteste.util.Ativo;
import com.mouts.mvteste.util.TipoConta;
import com.mouts.mvteste.util.TipoTransacao;
import com.mouts.mvteste.util.Util;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransacaoService {
	
	/*O atributo é final pois nao pode receber outra atribuicao por cima.*/
	private final TransacaoRepository transacaoRepository;
	
	private final ContaBancariaRepository contaBancariaRepository;
	
	/**
	 * Busca o valor somado da transação
	 *
	 * @param contaBancariaId: A conta bancaria.
	 * @return tipoTransacao: O tipo da transacao.
	 */
	private BigDecimal getSumVlrTransacao(Long contaBancariaId, TipoTransacao tipoTransacao) {
		LocalDate dataAtual =  LocalDate.now();	
		return transacaoRepository.sumVlrTransacaoByDateAndContaBancariaAndTipoTransacao(dataAtual, contaBancariaId, tipoTransacao);
	}
	
	/**
	 * Realiza uma validacao se o valor da transacao é maior que zero.
	 *
	 * @param transacao A transação de depósito/saque a ser registrada.
	 * @return Boolean.
	 */
	private Boolean checkValorTransacaoMaiorQueZero(Transacao transacao) {
		return transacao.getVlrTransacao().compareTo(BigDecimal.ZERO) > 0;
	}
	
	
	
	/**
	 * Realiza uma transação de depósito, registrando a entrada de dinheiro na conta.
	 * Para transações de depósito, não são realizadas validações de limite diário e saldo.
	 * Sera atualizado o saldo da conta bancaria.
	 * @param transacao A transação de depósito a ser registrada.
	 * @return A transação de depósito registrada.
	 */
	public Transacao depositar(Transacao transacao) {
		
		Optional<ContaBancaria> contaBancariaOpt = contaBancariaRepository.findById(transacao.getContaBancaria().getIdContaBancaria());
		
		if (!contaBancariaOpt.isPresent()) {
			throw new BussinessException("Nao existe uma conta para realizar o deposito");
		}
		ContaBancaria contaBancaria = contaBancariaOpt.get();
			
		if (!contaBancaria.getFlagAtivo().equals(Ativo.SIM)) {
			throw new BussinessException("A conta bancaria deve estar ativa");
		}
		
		//Validar se o tipo de transacao é realmente de Saque.
		if (!transacao.getTipoTransacao().equals(TipoTransacao.DEPOSITO)) {
			throw new BussinessException("O Tipo de transacao deve ser DEPOSITO");
		}
				
		if (!this.checkValorTransacaoMaiorQueZero(transacao)) {
			throw new BussinessException("O valor do deposito deve ser maior que zero");
		}
		
		BigDecimal novoVlrSaldo = contaBancaria.getVlrSaldo().add(transacao.getVlrTransacao());
		contaBancaria.setVlrSaldo(novoVlrSaldo); 		
		contaBancariaRepository.save(contaBancaria);
		
		return transacaoRepository.save(transacao);
	}
	
	/**
	 * Realiza uma transação de saque, registrando a saida de dinheiro na conta.
	 * Para transações de saque, são realizadas validações de limite diário e saldo.
	 * Sera atualizado o saldo da conta bancaria.
	 * @param transacao A transação de saque a ser registrada.
	 * @return A transação de saque registrada.
	 */
	public Transacao sacar(Transacao transacao) {
		
		Optional<ContaBancaria> contaBancariaOpt = contaBancariaRepository.findById(transacao.getContaBancaria().getIdContaBancaria());
		if (!contaBancariaOpt.isPresent()) {
			throw new BussinessException("Nao existe uma conta para realizar o saque");
		}
		
		//Validar se o tipo de transacao é realmente de Saque.
		if (!transacao.getTipoTransacao().equals(TipoTransacao.SAQUE)) {
			throw new BussinessException("O Tipo de transacao deve ser SAQUE");
		}
		
		if (!this.checkValorTransacaoMaiorQueZero(transacao)) {
			throw new BussinessException("O valor do saque deve ser maior que zero");
		}
		
		ContaBancaria contaBancaria = contaBancariaOpt.get();

		//Busca a soma de todas as transacoes de saque.
		BigDecimal sumVlrTransacao = this.getSumVlrTransacao(contaBancaria.getIdContaBancaria(), TipoTransacao.SAQUE);
		sumVlrTransacao = sumVlrTransacao.add(transacao.getVlrTransacao());
		//Verificar se nao ultrapassou o limite diario da transacao.
		if (!(contaBancaria.getVlrLimiteDiario().compareTo(sumVlrTransacao) > 0)) {
			throw new BussinessException("Saque nao foi realizado pois atingiu o valor limite diario de saques");
		}
		
		//Verificar se ainda tem saldo. Para as contas correntes permite deixar o valor negativo.
		if (!(contaBancaria.getVlrSaldo().compareTo(transacao.getVlrTransacao()) >= 0)) {
			
			if (!contaBancaria.getTipoConta().equals(TipoConta.CONTA_CORRENTE)) {
				throw new BussinessException("Saldo insuficiente para realizar o saque");
			}
		}
		BigDecimal novoVlrSaldo = contaBancaria.getVlrSaldo().subtract(transacao.getVlrTransacao());
		contaBancaria.setVlrSaldo(novoVlrSaldo); 		
		contaBancariaRepository.save(contaBancaria);
		//Se chegou até aqui entao, nao ultrapassou o limite diario de transacoes, e nao vai deixar negativo.
		return transacaoRepository.save(transacao);
	}
	
	/**
	 * Busca um extrato por ID pessoa e data de transacao inicio/fim, sem considerar as horas
	 * 
	 * @param idPessoa O ID da pessoa
	 * @param dataInicio Data de Inicio
	 * @param dataInicio Data Fim
	 * @return List<Transacao>
	 */
	public List<Transacao> getExtratoByIdPessoaAndDataTransacao(
            Long idPessoa, LocalDate dataInicio, LocalDate dataFim) {
		
		Util.checkPeriodoInformado(dataInicio, dataFim);
		Optional<List<Transacao>> transacaoOpt = transacaoRepository.findExtratoByIdPessoaAndPeriodo(idPessoa, dataInicio, dataFim);
		if (!transacaoOpt.isPresent()) {
			throw new BussinessException("Nao existe dados para gerar o extrato por Id Pessoa");
		}
		
        return transacaoOpt.get();
    }
	

	/**
	 * Busca um extrato por CPF e data de transacao inicio/fim, sem considerar as horas
	 * 
	 * @param cpf da pessoa
	 * @param dataInicio Data de Inicio
	 * @param dataInicio Data Fim
	 * @return List<Transacao>
	 */
	public List<Transacao> getExtratoByCpfAndDataTransacao(String cpf, LocalDate dataInicio, LocalDate dataFim) {
		
		Util.checkPeriodoInformado(dataInicio, dataFim);
		Optional<List<Transacao>> transacaoOpt = transacaoRepository.findExtratoByCpfPessoaAndPeriodo(cpf, dataInicio, dataFim);
		
		if (!transacaoOpt.isPresent()) {
			throw new BussinessException("Nao existe dados para gerar o extrato por cpf");
		}
        return transacaoOpt.get();
    }
	
}
