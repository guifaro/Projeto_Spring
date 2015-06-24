package com.sample.spring.controller;

import static com.sample.spring.compose.Compose.admin;
import static com.sample.spring.compose.Compose.tipoproduto;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.spring.ApplicationTest;
import com.sample.spring.controller.dto.TipoProdutoDTO;
import com.sample.spring.domain.Pagamento;
import com.sample.spring.domain.TipoProduto;
import com.sample.spring.domain.User;
import com.sample.spring.repository.TipoProdutoRepository;
import com.sample.spring.utils.MapperUtils;

public class TipoProdutoControllerTest extends ApplicationTest {

	private MapperUtils<TipoProduto, TipoProdutoDTO> convert = new MapperUtils<TipoProduto, TipoProdutoDTO>(TipoProduto.class, TipoProdutoDTO.class);

	@Autowired
	private TipoProdutoRepository tipoprodutoRepository;
	
	@Test
	public void testList() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		TipoProduto x2 = tipoproduto("SCANNER",Pagamento.AVULSO).build();
		saveall(x1, x2, xxx);

		signIn(xxx);

		Page<TipoProdutoDTO> page = get("/tipoproduto").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getContent(), contains(convert.toDTO(x2), convert.toDTO(x1)));
	}

	@Test
	public void testeListWithPagination() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		TipoProduto x2 = tipoproduto("SCANNER",Pagamento.AVULSO).build();
		TipoProduto x3 = tipoproduto("Cloud2",Pagamento.MENSAL).build();
		TipoProduto x4 = tipoproduto("SCANNER2",Pagamento.AVULSO).build();
		TipoProduto x5 = tipoproduto("Cloud3",Pagamento.MENSAL).build();
		saveall(x1, x2, x3, x4, x5, xxx);

		signIn(xxx);

		Page<TipoProdutoDTO> page = get("/tipoproduto").queryParam("page", "0").queryParam("size", "2").expectedStatus(HttpStatus.OK)
		    .getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(2));
		assertThat(page.getContent(), contains(convert.toDTO(x1), convert.toDTO(x2)));

		page = get("/tipoproduto").queryParam("size", "2").queryParam("page", "2").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(2));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(1));
		assertThat(page.getContent(), contains(convert.toDTO(x5)));
	}

	@Test
	public void testSearchByName() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		TipoProduto x2 = tipoproduto("SCANNER",Pagamento.AVULSO).build();
		saveall(x1, x2, xxx);

		signIn(xxx);

		Page<TipoProdutoDTO> page = get("/tipoproduto").queryParam("search", "loud").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getTotalElements(), equalTo(1L));
		assertThat(page.getContent(), contains(convert.toDTO(x1)));
	}

	@Test
	public void testSearchByPagamento() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		TipoProduto x2 = tipoproduto("SCANNER",Pagamento.AVULSO).build();
		saveall(x1, x2, xxx);

		signIn(xxx);

		Page<TipoProdutoDTO> page = get("/tipoproduto").queryParam("search", "MENSAL").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getTotalElements(), equalTo(1L));
		assertThat(page.getContent(), contains(convert.toDTO(x2)));
	}

	@Test
	public void testRead() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		saveall(x1, xxx);

		signIn(xxx);

		ResponseEntity<TipoProdutoDTO> response = get("/tipoproduto/%s", x1.getId()).expectedStatus(HttpStatus.OK).getResponse(TipoProdutoDTO.class);

		assertThat(response.getBody(), equalTo(convert.toDTO(x1)));
	}
	
	@Test
	public void testCreate() throws JsonProcessingException, IOException {
		User xxx = admin("xxx").build();
		saveall(xxx);
		signIn(xxx);

		String name = "Scanner";
		Pagamento pagamento = Pagamento.AVULSO;
		Boolean unico = false;

		TipoProduto tipoproduto = tipoproduto(name,pagamento).build();

		ResponseEntity<TipoProdutoDTO> response = post("/tipoproduto")
		    .json(convert.toDTO(tipoproduto))
		    .expectedStatus(HttpStatus.CREATED)
		    .getResponse(TipoProdutoDTO.class);

		assertThat(response.getBody().getId(), notNullValue());
		assertThat(response.getBody().getName(), equalTo(name));
		assertThat(response.getBody().getPagamento(), equalTo(pagamento));
		assertThat(response.getBody().getUnico(), equalTo(unico));

		assertThat(tipoprodutoRepository.findAll(), hasSize(1));
	}

	@Test
	public void testUpdate() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		saveall(x1, xxx);

		signIn(xxx);

		String name = "Scanner";
		Pagamento pagamento = Pagamento.AVULSO;
		Boolean unico = false;
		x1.setName(name);
		x1.setPagamento(pagamento);
		x1.setUnico(unico);

		ResponseEntity<TipoProdutoDTO> response = put("/tipoproduto/%s", x1.getId())
		    .json(x1)
		    .expectedStatus(HttpStatus.OK)
		    .getResponse(TipoProdutoDTO.class);

		assertThat(response.getBody().getName(), equalTo(name));
		assertThat(response.getBody().getPagamento(), equalTo(pagamento));
		assertThat(response.getBody().getUnico(), equalTo(unico));


		TipoProduto x = tipoprodutoRepository.findOne(x1.getId());

		assertThat(x.getName(), equalTo(name));
		assertThat(x.getPagamento(), equalTo(pagamento));
		assertThat(x.getUnico(), equalTo(unico));
	}

	@Test
	public void testDelete() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		saveall(x1, xxx);

		signIn(xxx);


		assertThat(tipoprodutoRepository.findAll(), hasSize(1));

		ResponseEntity<TipoProdutoDTO> response = delete("/tipoproduto/%s", x1.getId()).expectedStatus(HttpStatus.OK).getResponse(TipoProdutoDTO.class);

		assertThat(response.getBody().getId(), equalTo(x1.getId()));
		assertThat(tipoprodutoRepository.findAll(), hasSize(0));
	}
	
	@Test
	public void testReadNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		get("/tipoproduto/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testDeleteNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		delete("/tipoproduto/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testUpdateNotFound() {
		User xxx = admin("xxx").build();
		TipoProduto x1 = tipoproduto("Cloud",Pagamento.MENSAL).build();
		saveall(x1, xxx);
		signIn(xxx);

		put("/tipoproduto/%s", x1.getId() + 1)
		    .json(convert.toDTO(x1))
		    .expectedStatus(HttpStatus.NOT_FOUND)
		    .getResponse();
	}
	
	@Test
	public void test() {
		User xxx = admin("xxx").build();
		saveall(xxx);
		signIn(xxx);

	}
}
