package com.mouts.mvteste.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mouts.mvteste.model.Transacao;
import com.mouts.mvteste.util.TipoTransacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

	@Query("SELECT COALESCE(SUM(t.vlrTransacao), 0) FROM Transacao t " + "WHERE DATE_TRUNC('DAY',t.dataTransacao) = :date "
			+ "AND t.contaBancaria.idContaBancaria = :contaBancariaId " + "AND t.tipoTransacao = :tipoTransacao")
	BigDecimal sumVlrTransacaoByDateAndContaBancariaAndTipoTransacao(@Param("date")            LocalDate date,
																	 @Param("contaBancariaId") Long contaBancariaId, 
																	 @Param("tipoTransacao")   TipoTransacao tipoTransacao);

	@Query("SELECT t FROM Transacao t " + "JOIN FETCH t.contaBancaria cb " + "JOIN FETCH cb.pessoa p "
			+ "WHERE p.cpf = :cpf " + "AND DATE_TRUNC('DAY', t.dataTransacao) BETWEEN :startDate AND :endDate "
			+ "ORDER BY t.dataTransacao ASC")
	Optional<List<Transacao>> findExtratoByCpfPessoaAndPeriodo(@Param("cpf") String cpf,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT t FROM Transacao t " + "JOIN FETCH t.contaBancaria cb " + "JOIN FETCH cb.pessoa p "
			+ "WHERE p.idPessoa = :idPessoa " + "AND DATE_TRUNC('DAY', t.dataTransacao) BETWEEN :startDate AND :endDate "
			+ "ORDER BY t.dataTransacao ASC")
	Optional<List<Transacao>> findExtratoByIdPessoaAndPeriodo(@Param("idPessoa") Long idPessoa,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
