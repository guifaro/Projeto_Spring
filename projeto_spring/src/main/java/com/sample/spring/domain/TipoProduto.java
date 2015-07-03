package com.sample.spring.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tipoproduto")
@EqualsAndHashCode(exclude = "produtos")
@ToString(exclude = "produtos")

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
	
//1 para unico; 0 para nao unico
	@Column(name = "tipoproduto_unico", nullable = false)
	private Boolean unico;
	
	@OneToMany(mappedBy = "tipoproduto", fetch = FetchType.LAZY)
	private Set<Produto> produtos = new HashSet<Produto>();
}
