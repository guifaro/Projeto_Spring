package com.sample.spring.controller.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode

public class VendedorDTO {

	private Long id;

	@NotNull(message = "property name can't be null")
	private String name;
	
}
