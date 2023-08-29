package com.mouts.mvteste.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mouts.mvteste.exception.BussinessException;
import com.mouts.mvteste.model.ContaBancaria;
import com.mouts.mvteste.model.Pessoa;
import com.mouts.mvteste.repository.ContaBancariaRepository;
import com.mouts.mvteste.repository.PessoaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PessoaService {
	
	/*O atributo é final pois nao pode receber outra atribuicao por cima.*/
	private final PessoaRepository repository;
	private final ContaBancariaRepository cbRepository;
	
	
	private boolean validaCpf(Pessoa pessoa) {
		Boolean existeCpf = false;
		Optional<Pessoa> optPessoa = repository.findByCpf(pessoa.getCpf());
		
		if (optPessoa.isPresent()) {
			
			if (!optPessoa.get().getIdPessoa().equals(pessoa.getIdPessoa())) {
				existeCpf = true; 
			}
				
		}
		return existeCpf;
	}
	
	public Pessoa salvar(Pessoa pessoa) {
	
		if (this.validaCpf(pessoa)) {
			throw new BussinessException("O Cpf ja foi cadastrado");
		}
		
		return repository.save(pessoa);
	}
	
	public List<Pessoa> listarTodos() {
		return repository.findAll();
	}
	
	public void deletar(Long id) {
		cbRepository.findAllContaBancariaByPessoaId(id);
		
		Optional<Pessoa> pessoaOpt = this.buscarPorId(id);
		if (!pessoaOpt.isPresent()) {
			throw new BussinessException("A pessoa nao foi encontrada para ser excluida");
		}
		List<ContaBancaria> cbList = cbRepository.findAllContaBancariaByPessoaId(id);
		
		if (cbList.size() > 0) {
			throw new BussinessException("A pessoa nao pode ser excluida pois tem uma conta associada");
		}
		repository.delete(null);
	}
	
	public Optional<Pessoa> buscarPorId(Long id) {
		if (id == null) {
			throw new BussinessException("O id da pessoa deve ser informado");
		}
		Optional<Pessoa> pessoaOpt = repository.findById(id);
		
		

		return pessoaOpt;
	}
	
}
