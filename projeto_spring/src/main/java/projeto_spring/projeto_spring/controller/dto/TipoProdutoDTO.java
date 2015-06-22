package projeto_spring.projeto_spring.controller.dto;


import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode

public class TipoProdutoDTO {

	private Long id;

	@NotNull(message = "property name can't be null")
	private String name;

	@NotNull(message = "property state can't be null")
	private boolean pagamento;

	@NotNull(message = "property country can't be null")
	private boolean unico;
	
}
