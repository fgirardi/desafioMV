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

import com.mouts.mvteste.util.TipoTransacao;

import lombok.Data;

@Data
@Entity
@Table(name="transacao")
public class Transacao {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID_TRANSACAO")
	private Long idTransacao;

	@ManyToOne
	@JoinColumn(name = "ID_CONTA_BANCARIA")
	private ContaBancaria contaBancaria;
	
	@Column(name = "VLR_TRANSACAO")
	private BigDecimal vlrTransacao;
	
	@Column(name = "DATA_TRANSACAO")
	private LocalDateTime dataTransacao;
	
	@Column(name = "TIPO_TRANSACAO")
	@Enumerated(EnumType.STRING)
	private TipoTransacao tipoTransacao;
	
	@PrePersist
    public void prePersist() {
		dataTransacao = LocalDateTime.now();
    }

}
