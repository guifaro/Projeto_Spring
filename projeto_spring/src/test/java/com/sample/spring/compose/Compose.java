package com.sample.spring.compose;



import org.joda.time.LocalTime;

import com.sample.spring.domain.Pagamento;
import com.sample.spring.domain.TipoProduto;
import com.sample.spring.domain.TipoProduto.TipoProdutoBuilder;
import com.sample.spring.domain.Produto;
import com.sample.spring.domain.Produto.ProdutoBuilder;
import com.sample.spring.domain.User;
import com.sample.spring.domain.User.UserBuilder;
import com.sample.spring.domain.Vendedor;
import com.sample.spring.domain.Vendedor.VendedorBuilder;
import com.sample.spring.domain.Empresa;
import com.sample.spring.domain.Empresa.EmpresaBuilder;


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
	
	public static ProdutoBuilder produto(String name, TipoProduto tipoproduto){
		return Produto.builder().name(name).tipoproduto(tipoproduto);
	}
	
	public static VendedorBuilder vendedor(String name) {
		return Vendedor.builder().name(name);
	}
	
	public static EmpresaBuilder empresa(String name, Vendedor vendedor, String razao){
		return Empresa.builder().name(name).vendedor(vendedor).razao(razao).cnpj("27675892000187").diaCorte(05).diaFaturamento(15).dtCadastro(new LocalTime());
	}
	
}
