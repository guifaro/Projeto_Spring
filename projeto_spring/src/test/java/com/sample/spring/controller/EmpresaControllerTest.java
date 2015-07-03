package com.sample.spring.controller;

import static com.sample.spring.compose.Compose.admin;
import static com.sample.spring.compose.Compose.produto;
import static com.sample.spring.compose.Compose.tipoproduto;
import static com.sample.spring.compose.Compose.vendedor;
import static com.sample.spring.compose.Compose.empresa;
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
import com.sample.spring.controller.dto.EmpresaDTO;
import com.sample.spring.controller.dto.ProdutoDTO;
import com.sample.spring.domain.Empresa;
import com.sample.spring.domain.Pagamento;
import com.sample.spring.domain.Produto;
import com.sample.spring.domain.TipoProduto;
import com.sample.spring.domain.Vendedor;
import com.sample.spring.domain.User;
import com.sample.spring.repository.VendedorRepository;
import com.sample.spring.repository.EmpresaRepository;
import com.sample.spring.utils.MapperUtils;

public class EmpresaControllerTest extends ApplicationTest{
	
	private MapperUtils<Empresa, EmpresaDTO> convert = new MapperUtils<Empresa, EmpresaDTO>(Empresa.class, EmpresaDTO.class);

	@Autowired
	private EmpresaRepository repository;
		
	@Autowired
	private VendedorRepository vendedorRepository;
	
	@Test
	public void testList() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		Empresa icloud = empresa("Icloud", pedro, "Icloud Company").build();
		saveall(pedro, dropbox, icloud, guilherme);

		signIn(guilherme);

		Page<EmpresaDTO> page = get("/empresa").expectedStatus(HttpStatus.OK).getPage(EmpresaDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox), convert.toDTO(icloud)));
	}
	
	@Test
	public void testeListWithPagination() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa cloud = empresa("Cloud", pedro, "CloudComputing Ltd").build();
		Empresa boxcloud = empresa("Boxcloud", pedro, "Boxcloud Company").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		Empresa gcloud = empresa("Gcloud", pedro, "Gcloud Company Magnifc").build();
		Empresa icloud = empresa("Icloud", pedro, "Icloud Company").build();
				
		saveall(pedro, cloud, boxcloud, dropbox, gcloud, icloud, guilherme);

		signIn(guilherme);

		Page<EmpresaDTO> page = get("/empresa").queryParam("page", "0").queryParam("size", "2").expectedStatus(HttpStatus.OK)
		    .getPage(EmpresaDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(2));
		assertThat(page.getContent(), contains(convert.toDTO(boxcloud), convert.toDTO(cloud)));

		page = get("/empresa").queryParam("size", "2").queryParam("page", "2").expectedStatus(HttpStatus.OK).getPage(EmpresaDTO.class);

		assertThat(page.getNumber(), equalTo(2));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(1));
		assertThat(page.getContent(), contains(convert.toDTO(icloud)));
	}
	
	@Test
	public void testSearchByName() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		Empresa icloud = empresa("Icloud", pedro, "Icloud Company").build();
		saveall(pedro, dropbox, icloud, guilherme);

		signIn(guilherme);

		Page<EmpresaDTO> page = get("/empresa").queryParam("search", "Box").expectedStatus(HttpStatus.OK).getPage(EmpresaDTO.class);

		assertThat(page.getTotalElements(), equalTo(1L));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox)));
	}
	
	@Test
	public void testSearchByVendedorName() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		Empresa icloud = empresa("Icloud", pedro, "Icloud Company").build();
		saveall(pedro, dropbox, icloud, guilherme);

		signIn(guilherme);
		
		Page<EmpresaDTO> page = get("/empresa").queryParam("search", "reira").expectedStatus(HttpStatus.OK).getPage(EmpresaDTO.class);

		assertThat(page.getTotalElements(), equalTo(2L));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox), convert.toDTO(icloud)));
	}
	
	@Test
	public void testSearchByRazao() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		Empresa icloud = empresa("Icloud", pedro, "Icloud Company").build();
		saveall(pedro, dropbox, icloud, guilherme);

		signIn(guilherme);

		Page<EmpresaDTO> page = get("/empresa").queryParam("search", "pany").expectedStatus(HttpStatus.OK).getPage(EmpresaDTO.class);

		assertThat(page.getTotalElements(), equalTo(2L));
		assertThat(page.getContent(), contains(convert.toDTO(dropbox), convert.toDTO(icloud)));
	}
	
	@Test
	public void testRead() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		saveall(pedro, dropbox, guilherme);

		signIn(guilherme);

		ResponseEntity<EmpresaDTO> response = get("/empresa/" + dropbox.getId()).expectedStatus(HttpStatus.OK).getResponse(EmpresaDTO.class);
		EmpresaDTO dto = convert.toDTO(dropbox);
		assertThat(response.getBody(), equalTo(dto));
		assertThat(response.getBody().getVendedor(), equalTo(dto.getVendedor()));
	}
	
	@Test
	public void testCreate() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();		
		saveall(pedro, guilherme);

		signIn(guilherme);

		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();

		ResponseEntity<EmpresaDTO> response = post("/empresa")
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.CREATED)
		    .getResponse(EmpresaDTO.class);

		dropbox.setId(response.getBody().getId());
		EmpresaDTO dto = convert.toDTO(dropbox);
		assertThat(response.getBody(), equalTo(dto));
		assertThat(response.getBody().getVendedor(), equalTo(dto.getVendedor()));
		assertThat(repository.findAll(), hasSize(1));
	}

	@Test
	public void testCreateNotPersistyVendedor() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();		
		saveall(pedro, guilherme);

		signIn(guilherme);

		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		dropbox.getVendedor().setName("newname");

		ResponseEntity<EmpresaDTO> response = post("/empresa")
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.CREATED)
		    .getResponse(EmpresaDTO.class);

		assertThat(response.getBody().getVendedor().getName(), not("newname"));
		assertThat(vendedorRepository.findOne(pedro.getId()).getName(), not("newname"));
	}

	@Test
	public void testCreateInvalidVendedor() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();		
		saveall(pedro, guilherme);

		signIn(guilherme);

		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		dropbox.getVendedor().setId(9999l);

		post("/empresa")
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.NOT_FOUND)
		    .getResponse();

		assertThat(repository.findAll(), hasSize(0));

	}
	
	@Test
	public void testUpdate() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		saveall(pedro, dropbox, guilherme);

		signIn(guilherme);

		String name = "newname";
		dropbox.setName(name);

		ResponseEntity<EmpresaDTO> response = put("/empresa/%s", dropbox.getId())
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.OK)
		    .getResponse(EmpresaDTO.class);

		assertThat(response.getBody().getName(), equalTo(name));
		assertThat(response.getBody().getVendedor().getName(), equalTo("Pedro Pereira"));

		Empresa entity = repository.findByIdWithVendedor(dropbox.getId());

		assertThat(entity.getName(), equalTo(name));
		assertThat(entity.getVendedor().getName(),equalTo("Pedro Pereira"));
	}
	
	@Test
	public void testUpdateWithInvalidVendedor() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		saveall(pedro, dropbox, guilherme);

		signIn(guilherme);

		String name = "newname";
		dropbox.setName(name);
		dropbox.getVendedor().setId(9999l);

		put("/empresa/%s", dropbox.getId())
		    .json(convert.toDTO(dropbox))
		    .expectedStatus(HttpStatus.NOT_FOUND)
		    .getResponse();

		Empresa entity = repository.findByIdWithVendedor(dropbox.getId());
		assertThat(entity.getName(), not(name));
		assertThat(entity.getVendedor().getName(), equalTo("Pedro Pereira"));
	}

	@Test
	public void testDelete() {
		User guilherme = admin("guilherme").build();
		Vendedor pedro = vendedor("Pedro Pereira").build();
		Empresa dropbox = empresa("DropBox", pedro, "DropBox Company").build();
		saveall(pedro, dropbox, guilherme);

		signIn(guilherme);

		assertThat(repository.findAll(), hasSize(1));

		ResponseEntity<EmpresaDTO> response = delete("/empresa/%s", dropbox.getId()).expectedStatus(HttpStatus.OK).getResponse(
				EmpresaDTO.class);

		assertThat(response.getBody().getId(), equalTo(dropbox.getId()));
		assertThat(repository.findAll(), hasSize(0));
		assertThat(vendedorRepository.findAll(), hasSize(1));
	}

	@Test
	public void testReadNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		get("/empresa/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testDeleteNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		delete("/empresa/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testUpdateNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		put("/empresa/1")
		    .formParam("name", "name")
		    .expectedStatus(HttpStatus.NOT_FOUND);
	}

}


