package com.sample.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "empresaproduto")
@EqualsAndHashCode(exclude = {"empresa","produto","user"})
@ToString(exclude = {"empresa","produto","user"})

public class EmpresaProduto {
	
	@GeneratedValue
	@Id
	@Column(name = "empresaproduto_id")
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id", foreignKey = @ForeignKey(name = "FK_EMPRESA_ID"))
	private Empresa empresa;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "FK_PRODUTO_ID"))
	private Produto produto;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_ID"))
	private User user;

	@Column(name = "empresaproduto_dtcadastro", nullable = false)
	private LocalDate dtCadastro;
	
	@Column(name = "empresaproduto_iniCobranca", nullable = false)
	private LocalDate iniCobranca;
	
	@Column(name = "empresaproduto_fimCobranca", nullable = true)
	private LocalDate fimCobranca;

	@Column(name = "empresaproduto_Valor", nullable = false)
	private Float valor;
}
