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

import org.joda.time.LocalDate;

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
@Table(name = "empresa")
@EqualsAndHashCode(exclude = {"vendedor","produtos"})
@ToString(exclude = {"vendedor","produtos"})

public class Empresa {
	
	@GeneratedValue
	@Id
	@Column(name = "empresa_id")
	private Long id;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "vendedor_id", foreignKey = @ForeignKey(name = "FK_VENDEDOR_ID"))
	private Vendedor vendedor;

	@Column(name = "empresa_name", nullable = false, length = 200)
	private String name;

	@Column(name = "empresa_razao", nullable = false, length = 200)
	private String razao;
	
	@Column(name = "empresa_cnpj", nullable = false, length = 14)
	private String cnpj;
	
	@Column(name = "empresa_dtcorte", nullable = false, length = 2)
	private Integer diaCorte;
	
	@Column(name = "empresa_dtfaturamento", nullable = false, length = 2)
	private Integer diaFaturamento;
	
	@Column(name = "empresa_dtcadastro", nullable = false)
	private LocalDate dtCadastro;
	
	@OneToMany(mappedBy = "empresa")
	private Set<EmpresaProduto> produtos = new HashSet<EmpresaProduto>();
	
	
}
