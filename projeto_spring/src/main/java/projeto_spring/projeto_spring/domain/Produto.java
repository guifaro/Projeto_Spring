package projeto_spring.projeto_spring.domain;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "Produto")

public class Produto {
	
	@GeneratedValue
	@Id
	@Column(name = "Produto_id")
	private Long id;

	@Column(name = "Produto_name", nullable = false, length = 200)
	private String name;
	
//1 para mensal;0 para nao avulso
	@Column(name = "Produto_pagamento", nullable = false, length = 100)
	private boolean pagamento;
	
//1 para unico;0 para nao unico
	@Column(name = "TipoProduto_unico", nullable = false, length = 100)
	private boolean unico;

}
