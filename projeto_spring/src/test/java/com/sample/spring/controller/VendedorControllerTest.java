package com.sample.spring.controller;

import static com.sample.spring.compose.Compose.admin;
import static com.sample.spring.compose.Compose.vendedor;
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
import com.sample.spring.controller.dto.VendedorDTO;
import com.sample.spring.domain.Vendedor;
import com.sample.spring.domain.User;
import com.sample.spring.repository.VendedorRepository;
import com.sample.spring.utils.MapperUtils;

public class VendedorControllerTest extends ApplicationTest {

	private MapperUtils<Vendedor, VendedorDTO> convert = new MapperUtils<Vendedor, VendedorDTO>(Vendedor.class, VendedorDTO.class);

	@Autowired
	private VendedorRepository vendedorRepository;
	
	@Test
	public void testList() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		Vendedor paulo = vendedor("Paulo Arantes").build();
		saveall(roberto, paulo, guilherme);

		signIn(guilherme);

		Page<VendedorDTO> page = get("/vendedor").expectedStatus(HttpStatus.OK).getPage(VendedorDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getContent(), contains(convert.toDTO(paulo), convert.toDTO(roberto)));
	}

	@Test
	public void testeListWithPagination() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		Vendedor paulo = vendedor("Paulo Arantes").build();
		Vendedor fernando = vendedor("Fernando Pessoa").build();
		Vendedor camoes = vendedor("Camoes").build();
		Vendedor thiago = vendedor("Thiago Pedroso").build();
		saveall(roberto, paulo, fernando, camoes, thiago, guilherme);

		signIn(guilherme);

		Page<VendedorDTO> page = get("/vendedor").queryParam("page", "0").queryParam("size", "2").expectedStatus(HttpStatus.OK)
		    .getPage(VendedorDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(2));
		assertThat(page.getContent(), contains(convert.toDTO(camoes), convert.toDTO(fernando)));

		page = get("/vendedor").queryParam("size", "2").queryParam("page", "2").expectedStatus(HttpStatus.OK).getPage(VendedorDTO.class);

		assertThat(page.getNumber(), equalTo(2));
		assertThat(page.getSize(), equalTo(2));
		assertThat(page.getTotalElements(), equalTo(5l));
		assertThat(page.getTotalPages(), equalTo(3));
		assertThat(page.getContent(), hasSize(1));
		assertThat(page.getContent(), contains(convert.toDTO(thiago)));
	}

	@Test
	public void testSearchByName() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		Vendedor paulo = vendedor("Paulo Arantes").build();
		saveall(roberto, paulo, guilherme);

		signIn(guilherme);

		Page<VendedorDTO> page = get("/vendedor").queryParam("search", "Roberto").expectedStatus(HttpStatus.OK).getPage(VendedorDTO.class);

		assertThat(page.getNumber(), equalTo(0));
		assertThat(page.getSize(), equalTo(50));
		assertThat(page.getTotalElements(), equalTo(1L));
		assertThat(page.getContent(), contains(convert.toDTO(roberto)));
	}
	
	@Test
	public void testRead() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		saveall(roberto, guilherme);

		signIn(guilherme);

		ResponseEntity<VendedorDTO> response = get("/vendedor/%s", roberto.getId()).expectedStatus(HttpStatus.OK).getResponse(VendedorDTO.class);

		assertThat(response.getBody(), equalTo(convert.toDTO(roberto)));
	}
	
	@Test
	public void testCreate() throws JsonProcessingException, IOException {
		User guilherme = admin("guilherme").build();
		saveall(guilherme);
		signIn(guilherme);

		String name = "José Maria linguini";
		
		Vendedor vendedor = vendedor(name).build();

		ResponseEntity<VendedorDTO> response = post("/vendedor")
		    .json(convert.toDTO(vendedor))
		    .expectedStatus(HttpStatus.CREATED)
		    .getResponse(VendedorDTO.class);

		assertThat(response.getBody().getId(), notNullValue());
		assertThat(response.getBody().getName(), equalTo(name));

		assertThat(vendedorRepository.findAll(), hasSize(1));
	}
	
	@Test
	public void testCreateInvalid() throws JsonProcessingException, IOException {
		User guilherme = admin("guilherme").build();
		Vendedor jose = vendedor("Jose Maria").build();
		saveall(guilherme, jose);
		signIn(guilherme);

		String name = "Jose Maria";
		
		Vendedor vendedor = vendedor(name).build();

		post("/vendedor")
		    .json(convert.toDTO(vendedor))
		    .expectedStatus(HttpStatus.PRECONDITION_FAILED)
		    .getResponse(VendedorDTO.class);
	}

	@Test
	public void testUpdate() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		saveall(roberto, guilherme);

		signIn(guilherme);

		String name = "Fernando Pessoa";
		roberto.setName(name);

		ResponseEntity<VendedorDTO> response = put("/vendedor/%s", roberto.getId())
		    .json(roberto)
		    .expectedStatus(HttpStatus.OK)
		    .getResponse(VendedorDTO.class);

		assertThat(response.getBody().getName(), equalTo(name));

		Vendedor fernando = vendedorRepository.findOne(roberto.getId());

		assertThat(fernando.getName(), equalTo(name));
	}
	
	@Test
	public void testUpdateInvalid() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		Vendedor pedro = vendedor("Pedro Arantes").build();
		saveall(roberto, pedro, guilherme);

		signIn(guilherme);

		String name = "Pedro Arantes";
		roberto.setName(name);

		put("/vendedor/%s", roberto.getId() + 1)
	    .json(convert.toDTO(roberto))
	    .expectedStatus(HttpStatus.PRECONDITION_FAILED)
	    .getResponse();
	}

	@Test
	public void testDelete() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		saveall(roberto, guilherme);

		signIn(guilherme);


		assertThat(vendedorRepository.findAll(), hasSize(1));

		ResponseEntity<VendedorDTO> response = delete("/vendedor/%s", roberto.getId()).expectedStatus(HttpStatus.OK).getResponse(VendedorDTO.class);

		assertThat(response.getBody().getId(), equalTo(roberto.getId()));
		assertThat(vendedorRepository.findAll(), hasSize(0));
	}
	
	@Test
	public void testReadNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		get("/vendedor/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testDeleteNotFound() {
		User bruno = admin("bruno").build();
		saveall(bruno);
		signIn(bruno);
		delete("/vendedor/1").expectedStatus(HttpStatus.NOT_FOUND).getResponse();
	}

	@Test
	public void testUpdateNotFound() {
		User guilherme = admin("guilherme").build();
		Vendedor roberto = vendedor("Roberto Carlos").build();
		saveall(roberto, guilherme);

		signIn(guilherme);

		put("/vendedor/%s", roberto.getId() + 1)
		    .json(convert.toDTO(roberto))
		    .expectedStatus(HttpStatus.NOT_FOUND)
		    .getResponse();
	}
}
