package com.mouts.mvteste.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mouts.mvteste.model.ContaBancaria;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

	@Query("SELECT cb FROM ContaBancaria cb " + "WHERE cb.pessoa.idPessoa = :idPessoa")
	List<ContaBancaria> findAllContaBancariaByPessoaId(@Param("idPessoa") Long idPessoa);
}
