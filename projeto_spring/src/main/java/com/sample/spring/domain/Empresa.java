package com.sample.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@EqualsAndHashCode(exclude = "vendedor")
@ToString(exclude = "vendedor")

public class Empresa {
	
	@GeneratedValue
	@Id
	@Column(name = "empresa_id")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "vendedor_id", foreignKey = @ForeignKey(name = "FK_VENDEDOR_ID"))
	private Vendedor vendedor;

	@Column(name = "empresa_name", nullable = false, length = 200)
	private String name;

	@Column(name = "empresa_razao", nullable = false, length = 200)
	private String razao;
	
	@Column(name = "empresa_cnpj", nullable = false, length = 14)
	private String cnpj;
	
	@Column(name = "empresa_dtcorte", nullable = false, length = 2)
	private int diaCorte;
	
	@Column(name = "empresa_dtfaturamento", nullable = false, length = 2)
	private int diaFaturamento;
	
	@Column(name = "empresa_dtcadastro", nullable = false)
	private LocalDate dtCadastro;
	
	
}
