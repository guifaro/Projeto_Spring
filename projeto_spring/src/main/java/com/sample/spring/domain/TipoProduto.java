package com.sample.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tipoproduto")
public class TipoProduto {

	@GeneratedValue
	@Id
	@Column(name = "tipoproduto_id")
	private Long id;

	@Column(name = "tipoproduto_name", nullable = false, length = 200)
	private String name;
	
	@Column(name = "tipoproduto_pagamento", nullable = false)
	@Enumerated(EnumType.STRING)
	private Pagamento pagamento;
	
//1 para unico;0 para nao unico
	@Column(name = "tipoproduto_unico", nullable = false)
	private Boolean unico;
}
