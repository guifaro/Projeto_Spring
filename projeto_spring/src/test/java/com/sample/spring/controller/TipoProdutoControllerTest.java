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
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.AVULSO).build();
		
		saveall(scanner, cloud, guilherme);

		signIn(guilherme);

		Page<TipoProdutoDTO> page = get("/tipoproduto").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getContent(), contains(convert.toDTO(cloud), convert.toDTO(scanner)));
	}

	@Test
	public void testeListWithPagination() {
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.AVULSO).build();
		TipoProduto notebook = tipoproduto("Notebook",Pagamento.MENSAL).build();
		TipoProduto browser = tipoproduto("Browser",Pagamento.MENSAL).build();
		TipoProduto webbrowser = tipoproduto("WebBrowser",Pagamento.AVULSO).build();
		saveall(scanner, cloud, notebook, browser, webbrowser, guilherme);

		signIn(guilherme);

		Page<TipoProdutoDTO> page = get("/tipoproduto").queryParam("page", "0").queryParam("size", "2").expectedStatus(HttpStatus.OK)
		    .getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(2));
		assertThat(page.getContent(), contains(convert.toDTO(browser), convert.toDTO(cloud)));

		page = get("/tipoproduto").queryParam("size", "2").queryParam("page", "2").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(2));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(1));
		assertThat(page.getContent(), contains(convert.toDTO(webbrowser)));
	}

	@Test
	public void testSearchByName() {
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.AVULSO).build();		
		saveall(scanner, cloud, guilherme);

		signIn(guilherme);

		Page<TipoProdutoDTO> page = get("/tipoproduto").queryParam("search", "NNER").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getTotalElements(), equalTo(1L));
		assertThat(page.getContent(), contains(convert.toDTO(scanner)));
	}

	@Test
	public void testSearchByPagamento() {
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.AVULSO).build();		
		saveall(scanner, cloud, guilherme);

		signIn(guilherme);

		Page<TipoProdutoDTO> page = get("/tipoproduto").queryParam("search", "MENSAL").expectedStatus(HttpStatus.OK).getPage(TipoProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getTotalElements(), equalTo(1L));
		assertThat(page.getContent(), contains(convert.toDTO(scanner)));
	}

	@Test
	public void testRead() {
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		saveall(scanner, guilherme);

		signIn(guilherme);

		ResponseEntity<TipoProdutoDTO> response = get("/tipoproduto/%s", scanner.getId()).expectedStatus(HttpStatus.OK).getResponse(TipoProdutoDTO.class);

		assertThat(response.getBody(), equalTo(convert.toDTO(scanner)));
	}
	
	@Test
	public void testCreate() throws JsonProcessingException, IOException {
		User guilherme = admin("guilherme").build();
		saveall(guilherme);
		signIn(guilherme);

		String name = "Scanner";
		Pagamento pagamento = Pagamento.AVULSO;
		Boolean unico = true;

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
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		saveall(scanner, guilherme);

		signIn(guilherme);

		String name = "Scanner";
		Pagamento pagamento = Pagamento.AVULSO;
		Boolean unico = false;
		scanner.setName(name);
		scanner.setPagamento(pagamento);
		scanner.setUnico(unico);

		ResponseEntity<TipoProdutoDTO> response = put("/tipoproduto/%s", scanner.getId())
		    .json(scanner)
		    .expectedStatus(HttpStatus.OK)
		    .getResponse(TipoProdutoDTO.class);

		assertThat(response.getBody().getName(), equalTo(name));
		assertThat(response.getBody().getPagamento(), equalTo(pagamento));
		assertThat(response.getBody().getUnico(), equalTo(unico));


		TipoProduto x = tipoprodutoRepository.findOne(scanner.getId());

		assertThat(x.getName(), equalTo(name));
		assertThat(x.getPagamento(), equalTo(pagamento));
		assertThat(x.getUnico(), equalTo(unico));
	}

	@Test
	public void testDelete() {
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		saveall(scanner, guilherme);

		signIn(guilherme);


		assertThat(tipoprodutoRepository.findAll(), hasSize(1));

		ResponseEntity<TipoProdutoDTO> response = delete("/tipoproduto/%s", scanner.getId()).expectedStatus(HttpStatus.OK).getResponse(TipoProdutoDTO.class);

		assertThat(response.getBody().getId(), equalTo(scanner.getId()));
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
		User guilherme = admin("guilherme").build();
		TipoProduto scanner = tipoproduto("SCANNER",Pagamento.MENSAL).build();
		saveall(scanner, guilherme);

		signIn(guilherme);

		put("/tipoproduto/%s", scanner.getId() + 1)
		    .json(convert.toDTO(scanner))
		    .expectedStatus(HttpStatus.NOT_FOUND)
		    .getResponse();
	}
}
