package com.mouts.mvteste.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Classe que representa uma conta bancária.
 * Esta classe armazena informações sobre o saldo, limite diário,
 * status de ativação/inativação, tipo da conta e a pessoa associada à conta.
 */

@Data
@Entity
@Table(name="pessoa")
public class Pessoa {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID_PESSOA")
	private Long idPessoa;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "cpf")
	private String cpf;
		
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento; 

}
