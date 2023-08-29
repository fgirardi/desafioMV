package com.mouts.mvteste.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.mouts.mvteste.util.Ativo;
import com.mouts.mvteste.util.TipoConta;

import lombok.Data;

@Data
@Entity
@Table(name="conta_bancaria")
public class ContaBancaria {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID_CONTA_BANCARIA")
	private Long idContaBancaria;
	
	@Column(name = "VLR_SALDO")
	private BigDecimal vlrSaldo;
	
	@Column(name = "VLR_LIMITE_DIARIO")
	private BigDecimal vlrLimiteDiario;
	
	@Column(name = "IDT_ATIVO")
	@Enumerated(EnumType.STRING)
	private Ativo flagAtivo;
	
	@Column(name = "TIPO_CONTA")
	@Enumerated(EnumType.STRING)
	private TipoConta tipoConta;
	
	@ManyToOne
	@JoinColumn(name = "ID_PESSOA")
	private Pessoa pessoa;
	
	@Column(name = "DATA_CRIACAO")
	private LocalDateTime dataCriacao;
	
	@PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
    }
	
}
