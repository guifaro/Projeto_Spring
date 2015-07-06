package com.sample.spring.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "produto")
@EqualsAndHashCode(exclude = {"tipoproduto","empresas"})
@ToString(exclude = {"tipoproduto","empresas"})

public class Produto {
	
	@GeneratedValue
	@Id
	@Column(name = "produto_id")
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "tipoproduto_id", foreignKey = @ForeignKey(name = "FK_TIPO_PRODUTO"))
	private TipoProduto tipoproduto;

	@Column(name = "produto_name", nullable = false, length = 200)
	private String name;
	
	@OneToMany(mappedBy = "produto")
	private Set<EmpresaProduto> empresas = new HashSet<EmpresaProduto>();

}
