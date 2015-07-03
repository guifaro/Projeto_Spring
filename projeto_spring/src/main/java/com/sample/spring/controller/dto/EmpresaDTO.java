package com.sample.spring.controller.dto;

import javax.validation.constraints.NotNull;





import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "vendedor")
@ToString(exclude = "vendedor")

public class EmpresaDTO {
	
	private Long id;

	@NotNull(message = "property name can't be null")
	private String name;

	private VendedorDTO vendedor;
	
	@NotNull(message = "property name can't be null")
	private String razao;
	
	@NotNull(message = "property name can't be null")
	private String cnpj;
	
	@NotNull(message = "property name can't be null")
	private Integer diaCorte;
	
	@NotNull(message = "property name can't be null")
	private Integer diaFaturamento;
	
	/*@NotNull(message = "property name can't be null")
	private LocalTime dtCadastro;*/

}
