package com.sample.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "vendedor")

public class Vendedor {
	
	@GeneratedValue
	@Id
	@Column(name = "vendedor_id")
	private Long id;

	@Column(name = "vendedor_name", nullable = false, length = 200)
	private String name;

}
