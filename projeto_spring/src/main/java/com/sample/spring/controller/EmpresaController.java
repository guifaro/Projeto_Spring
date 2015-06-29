package com.sample.spring.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sample.spring.controller.dto.EmpresaDTO;
import com.sample.spring.domain.Vendedor;
import com.sample.spring.domain.Empresa;
import com.sample.spring.exception.NotFoundException;
import com.sample.spring.repository.VendedorRepository;
import com.sample.spring.repository.EmpresaRepository;
import com.sample.spring.security.Roles;
import com.sample.spring.utils.MapperUtils;
import com.sample.spring.utils.SQLLikeUtils;

@RestController
@RequestMapping("/empresa")
@Secured(Roles.ROLE_ADMIN)

public class EmpresaController {
	private MapperUtils<Empresa, EmpresaDTO> convert = new MapperUtils<Empresa, EmpresaDTO>(Empresa.class, EmpresaDTO.class);
	
	@Autowired
	private EmpresaRepository repository;
	
	@Autowired
	private VendedorRepository vendedorRepository;
	
	
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	public Page<EmpresaDTO> list(@PageableDefault(page = 0, size = 50, sort = "name") Pageable page,
	    @RequestParam(value = "search", required = false) String search) {

		Page<Empresa> result;
		if (StringUtils.hasText(search)) {
			result = repository.search(SQLLikeUtils.like(search), page);
		} else {
			result = repository.findAll(page);
		}

		return new PageImpl<EmpresaDTO>(result.getContent().stream().map(c -> convert.toDTO(c)).collect(Collectors.toList()),
		    page, result.getTotalElements());
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{ref}", method = RequestMethod.GET)
	public EmpresaDTO read(@PathVariable("ref") Long ref) {

		Empresa entity = repository.findByIdWithVendedor(ref);

		if (entity == null) {
			throw new NotFoundException(Empresa.class);
		}
         EmpresaDTO x = convert.toDTO(entity);
		return x;
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public EmpresaDTO create(@Valid @RequestBody EmpresaDTO dto) {

		Vendedor vendedor = vendedorRepository.findOne(dto.getVendedor().getId());
		if (vendedor == null) {
			throw new NotFoundException(Vendedor.class);
		}

		Empresa entity = convert.toEntity(dto);
		entity.setVendedor(vendedor);

		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{ref}", method = RequestMethod.PUT)
	public EmpresaDTO update(@PathVariable("ref") Long ref, @Valid @RequestBody EmpresaDTO dto) {

		Vendedor vendedor = vendedorRepository.findOne(dto.getVendedor().getId());
		if (vendedor == null) {
			throw new NotFoundException(Vendedor.class);
		}

		Empresa entity = repository.findOne(ref);
		if (entity == null) {
			throw new NotFoundException(Empresa.class);
		}

		convert.updateEntity(entity, dto, "id", "vendedor");
		entity.setVendedor(vendedor);

		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{ref}", method = RequestMethod.DELETE)
	public EmpresaDTO delete(@PathVariable("ref") Long ref) {

		Empresa entity = repository.findOne(ref);
		if (entity == null) {
			throw new NotFoundException(Empresa.class);
		}

		this.repository.delete(entity);

		return convert.toDTO(entity);
	}
}
