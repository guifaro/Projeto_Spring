package com.sample.spring.compose;

import java.util.concurrent.atomic.AtomicLong;

import com.sample.spring.domain.Pagamento;
import com.sample.spring.domain.TipoProduto;
import com.sample.spring.domain.TipoProduto.TipoProdutoBuilder;
import com.sample.spring.domain.User;
import com.sample.spring.domain.User.UserBuilder;

public class Compose {

	private static AtomicLong counter = new AtomicLong();

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
