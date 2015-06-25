package com.sample.spring.controller.dto;


import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "tipoproduto")
@ToString(exclude = "tipoproduto")

public class ProdutoDTO {
	private Long id;

	@NotNull(message = "property name can't be null")
	private String name;

	private TipoProdutoDTO tipoproduto;
}
