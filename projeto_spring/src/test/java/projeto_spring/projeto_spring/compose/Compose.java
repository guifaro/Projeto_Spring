package projeto_spring.projeto_spring.compose;

import projeto_spring.projeto_spring.domain.Pagamento;
import projeto_spring.projeto_spring.domain.TipoProduto;
import projeto_spring.projeto_spring.domain.TipoProduto.TipoProdutoBuilder;
import projeto_spring.projeto_spring.domain.User;
import projeto_spring.projeto_spring.domain.User.UserBuilder;



public class Compose {


	public static UserBuilder admin(String name) {
		return user(name).admin(true);
	}

	public static UserBuilder user(String name) {
		return User.builder().name(name).password(name).login(name).admin(false);
	}

	public static TipoProdutoBuilder tipoproduto(String name, Pagamento pagamento) {
		return TipoProduto.builder().name(name).pagamento(pagamento).unico(true);
	}

	
}
