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
@Table(name = "produto")

public class Produto {
	
	@GeneratedValue
	@Id
	@Column(name = "produto_id")
	private Long id;

	@Column(name = "produto_name", nullable = false, length = 200)
	private String name;
	

}
