package com.sample.spring.domain;

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
@Table(name = "vendedor")
@EqualsAndHashCode(exclude = "empresas")
@ToString(exclude = "empresas")

public class Vendedor {
	
	@GeneratedValue
	@Id
	@Column(name = "vendedor_id")
	private Long id;

	@Column(name = "vendedor_name", nullable = false, length = 200)
	private String name;

	@OneToMany(mappedBy = "vendedor")
	private Set<Empresa> empresas = new HashSet<Empresa>();
}
