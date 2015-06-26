package com.sample.spring.controller;

import static com.sample.spring.compose.Compose.admin;
import static com.sample.spring.compose.Compose.tipoproduto;
import static com.sample.spring.compose.Compose.produto;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sample.spring.ApplicationTest;
import com.sample.spring.controller.dto.ProdutoDTO;
import com.sample.spring.domain.Pagamento;
import com.sample.spring.domain.Produto;
import com.sample.spring.domain.TipoProduto;
import com.sample.spring.domain.User;
import com.sample.spring.repository.TipoProdutoRepository;
import com.sample.spring.repository.ProdutoRepository;
import com.sample.spring.utils.MapperUtils;


public class ProdutoControllerTest extends ApplicationTest {

	private MapperUtils<Produto, ProdutoDTO> convert = new MapperUtils<Produto, ProdutoDTO>(Produto.class, ProdutoDTO.class);

	@Autowired
	private ProdutoRepository repository;
		
	@Autowired
	private TipoProdutoRepository tipoprodutoRepository;
	
	@Test
	public void testList() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		Produto icloud = produto("Icloud", cloud).build();
		saveall(cloud, dropbox, icloud, guilherme);

		signIn(guilherme);

		Page<ProdutoDTO> page = get("/produto").expectedStatus(HttpStatus.OK).getPage(ProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox), convert.toDTO(icloud)));
	}

	@Test
	public void testeListWithPagination() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto boxcloud = produto("BoxCloud", cloud).build();
		Produto dropbox = produto("DropBox", cloud).build();
		Produto gcloud = produto("Gcloud", cloud).build();
		Produto icloud = produto("Icloud", cloud).build();
		Produto mbox = produto("mBox", cloud).build();

		
		saveall(cloud, boxcloud, dropbox, gcloud, icloud, mbox, guilherme);

		signIn(guilherme);

		Page<ProdutoDTO> page = get("/produto").queryParam("page", "0").queryParam("size", "2").expectedStatus(HttpStatus.OK)
		    .getPage(ProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(2));
		assertThat(page.getContent(), contains(convert.toDTO(boxcloud), convert.toDTO(dropbox)));

		page = get("/produto").queryParam("size", "2").queryParam("page", "2").expectedStatus(HttpStatus.OK).getPage(ProdutoDTO.class);

		assertThat(page.getNumber(), equalTo(2));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(1));
		assertThat(page.getContent(), contains(convert.toDTO(mbox)));
	}
	
	public void testSearchByName() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		Produto icloud = produto("Icloud", cloud).build();
		saveall(cloud, dropbox, icloud, guilherme);

		signIn(guilherme);

		Page<ProdutoDTO> page = get("/produto").queryParam("search", "Box").expectedStatus(HttpStatus.OK).getPage(ProdutoDTO.class);

		assertThat(page.getTotalElements(), equalTo(1L));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox)));
	}
	
	@Test
	public void testSearchByTipoProdutoName() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		Produto icloud = produto("Icloud", cloud).build();
		saveall(cloud, dropbox, icloud, guilherme);

		signIn(guilherme);

		Page<ProdutoDTO> page = get("/produto").queryParam("search", "Clou").expectedStatus(HttpStatus.OK).getPage(ProdutoDTO.class);

		assertThat(page.getTotalElements(), equalTo(2L));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox), convert.toDTO(icloud)));
	}
	
	@Test
	public void testSearchByTipoProdutoPagamento() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		Produto icloud = produto("Icloud", cloud).build();
		saveall(cloud, dropbox, icloud, guilherme);

		signIn(guilherme);

		Page<ProdutoDTO> page = get("/produto").queryParam("search", "SAL").expectedStatus(HttpStatus.OK).getPage(ProdutoDTO.class);

		assertThat(page.getTotalElements(), equalTo(2L));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox), convert.toDTO(icloud)));
	}
	
	@Test
	public void testRead() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		saveall(cloud, dropbox, guilherme);

		signIn(guilherme);

		ResponseEntity<ProdutoDTO> response = get("/produto/" + dropbox.getId()).expectedStatus(HttpStatus.OK).getResponse(ProdutoDTO.class);
		ProdutoDTO dto = convert.toDTO(dropbox);
		assertThat(response.getBody(), equalTo(dto));
		assertThat(response.getBody().getTipoproduto(), equalTo(dto.getTipoproduto()));
	}

	@Test
	public void testCreate() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		saveall(cloud, guilherme);

		signIn(guilherme);

		Produto dropbox = produto("Dropbox", cloud).build();

		ResponseEntity<ProdutoDTO> response = post("/produto")
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.CREATED)
		    .getResponse(ProdutoDTO.class);

		dropbox.setId(response.getBody().getId());
		ProdutoDTO dto = convert.toDTO(dropbox);
		assertThat(response.getBody(), equalTo(dto));
		assertThat(response.getBody().getTipoproduto(), equalTo(dto.getTipoproduto()));
		assertThat(repository.findAll(), hasSize(1));
	}

	@Test
	public void testCreateNotPersistyTipoProduto() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		saveall(cloud, guilherme);

		signIn(guilherme);
		
		Produto dropbox = produto("Dropbox", cloud).build();
		dropbox.getTipoproduto().setName("newname");

		ResponseEntity<ProdutoDTO> response = post("/produto")
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.CREATED)
		    .getResponse(ProdutoDTO.class);

		assertThat(response.getBody().getTipoproduto().getName(), not("newname"));
		assertThat(tipoprodutoRepository.findOne(cloud.getId()).getName(), not("newname"));
	}

	@Test
	public void testCreateInvalidTipoProduto() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		saveall(cloud, guilherme);

		signIn(guilherme);

		Produto dropbox = produto("Dropbox", cloud).build();
		dropbox.getTipoproduto().setId(9999l);

		post("/produto")
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.NOT_FOUND)
		    .getResponse();

		assertThat(repository.findAll(), hasSize(0));

	}
	
	@Test
	public void testUpdate() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		saveall(cloud, dropbox, guilherme);

		signIn(guilherme);

		String name = "newname";
		dropbox.setName(name);

		ResponseEntity<ProdutoDTO> response = put("/produto/%s", dropbox.getId())
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.OK)
		    .getResponse(ProdutoDTO.class);

		assertThat(response.getBody().getName(), equalTo(name));
		assertThat(response.getBody().getTipoproduto().getName(), equalTo("Cloud"));

		Produto entity = repository.findByIdWithTipoProduto(dropbox.getId());

		assertThat(entity.getName(), equalTo(name));
		assertThat(entity.getTipoproduto().getName(),equalTo("Cloud"));
	}

	@Test
	public void testUpdateWithInvalidTipoProduto() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		saveall(cloud, dropbox, guilherme);

		signIn(guilherme);

		String name = "newname";
		dropbox.setName(name);
		dropbox.getTipoproduto().setId(9999l);

		put("/produto/%s", dropbox.getId())
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.NOT_FOUND)
		    .getResponse();

		Produto entity = repository.findByIdWithTipoProduto(dropbox.getId());
		assertThat(entity.getName(), not(name));
		assertThat(entity.getTipoproduto().getName(), equalTo("Cloud"));
	}

	@Test
	public void testDelete() {
		User guilherme = admin("guilherme").build();
		TipoProduto cloud = tipoproduto("Cloud",Pagamento.MENSAL).build();
		Produto dropbox = produto("DropBox", cloud).build();
		saveall(cloud, dropbox, guilherme);

		signIn(guilherme);

		assertThat(repository.findAll(), hasSize(1));

		ResponseEntity<ProdutoDTO> response = delete("/produto/%s", dropbox.getId()).expectedStatus(HttpStatus.OK).getResponse(
		    ProdutoDTO.class);

		assertThat(response.getBody().getId(), equalTo(dropbox.getId()));
		assertThat(repository.findAll(), hasSize(0));
		assertThat(tipoprodutoRepository.findAll(), hasSize(1));
	}

	@Test
	public void testReadNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		get("/produto/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testDeleteNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		delete("/produto/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testUpdateNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		put("/produto/1")
		    .formParam("name", "name")
		    .expectedStatus(HttpStatus.NOT_FOUND);
	}

}
