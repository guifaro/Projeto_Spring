package com.sample.spring.controller.dto;


import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sample.spring.domain.Pagamento;

@Data
@NoArgsConstructor
@EqualsAndHashCode

public class TipoProdutoDTO {

	private Long id;

	@NotNull(message = "property name can't be null")
	private String name;

	@NotNull(message = "property state can't be null")
	private Pagamento pagamento;

	@NotNull(message = "property country can't be null")
	private Boolean unico;
	
}
