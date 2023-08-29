package com.mouts.mvteste.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mouts.mvteste.exception.BussinessException;
import com.mouts.mvteste.model.ContaBancaria;
import com.mouts.mvteste.model.Pessoa;
import com.mouts.mvteste.repository.ContaBancariaRepository;
import com.mouts.mvteste.repository.PessoaRepository;
import com.mouts.mvteste.util.Ativo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ContaBancariaService {
	
	/*O atributo é final pois nao pode receber outra atribuicao por cima.*/
	private final ContaBancariaRepository repository;
	private final PessoaRepository pessoaRepository;
	
	private ContaBancaria getContaBancaria(Long id) {

		Optional<ContaBancaria> optContaBancaria = this.buscarPorId(id);
		
		if (!optContaBancaria.isPresent()) {
			throw new BussinessException("A conta bancaria nao existe");
		}
		ContaBancaria contaBancaria = optContaBancaria.get();
		
		return contaBancaria;
	}
	
	public ContaBancaria criarConta(ContaBancaria contaBancaria) {
		
		if (contaBancaria.getVlrLimiteDiario().equals(BigDecimal.ZERO)) {
			throw new BussinessException("Nao é permitido criar uma conta cujo o valor de limite diario e zero");
		}
		
		if (contaBancaria.getFlagAtivo().equals(Ativo.NAO)) {
			throw new BussinessException("Nao é permitido criar uma conta bancaria INATIVA");
		}
		
		Optional<Pessoa> pessoaOpt = pessoaRepository.findByIdPessoa(contaBancaria.getPessoa().getIdPessoa());
		if (!pessoaOpt.isPresent()) {
			throw new BussinessException("Nao é possivel criar a conta pois a pessoa nao existe");
		}
		
		List<ContaBancaria> contaBancariaList = repository.findAllContaBancariaByPessoaId(contaBancaria.getPessoa().getIdPessoa());
		boolean contaJaExiste = contaBancariaList
								.stream()
								.anyMatch(conta -> conta.getTipoConta().equals(contaBancaria.getTipoConta()));
		
		if (contaJaExiste) {
			throw new BussinessException("Ja foi criado uma conta para esta pessoa com este tipo");
		}
		
		return repository.save(contaBancaria);
	}
	
	public List<ContaBancaria> listarTodasContasPorPessoa(Long idPessoa) {
		return repository.findAllContaBancariaByPessoaId(idPessoa);
	}
	
	public void deletar(Long id) {
		repository.delete(null);
	}
	
	public Optional<ContaBancaria> buscarPorId(Long id) {
		return repository.findById(id);
	}
	
	public ContaBancaria bloquearPorId(Long id) {
		
		ContaBancaria contaBancaria = this.getContaBancaria(id);
		
		if (contaBancaria.getFlagAtivo().equals(Ativo.NAO)) {
			throw new BussinessException("A conta ja esta inativa");
		}
		
		contaBancaria.setFlagAtivo(Ativo.NAO);
		return repository.save(contaBancaria);
		
	}
	
	public ContaBancaria desbloquearPorId(Long id) {
		
		ContaBancaria contaBancaria = this.getContaBancaria(id);
		
		if (contaBancaria.getFlagAtivo().equals(Ativo.SIM)) {
			throw new BussinessException("A conta ja esta ativa");
		}
		
		contaBancaria.setFlagAtivo(Ativo.SIM);
		return repository.save(contaBancaria);
		
	}
	
	
}
