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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sample.spring.controller.dto.VendedorDTO;
import com.sample.spring.domain.Vendedor;
import com.sample.spring.exception.NotFoundException;
import com.sample.spring.exception.WebException;
import com.sample.spring.repository.VendedorRepository;
import com.sample.spring.security.Roles;
import com.sample.spring.utils.MapperUtils;
import com.sample.spring.utils.SQLLikeUtils;

@RestController
@RequestMapping("/vendedor")
@Secured(Roles.ROLE_ADMIN)

public class VendedorController {
	
	private MapperUtils<Vendedor, VendedorDTO> convert = new MapperUtils<Vendedor, VendedorDTO>(Vendedor.class, VendedorDTO.class);
	
	@Autowired
	private VendedorRepository repository;
	
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	public Page<VendedorDTO> list(@PageableDefault(page = 0, size = 50, sort = "name") Pageable page,
		    @RequestParam(value = "search", required = false) String search) {

			Page<Vendedor> result;
			if (StringUtils.hasText(search)) {
				result = repository.search(SQLLikeUtils.like(search), page);
			} else {
				result = repository.findAll(page);
			}
			
			return new PageImpl<VendedorDTO>(result.getContent().stream().map(c -> convert.toDTO(c)).collect(Collectors.toList()),
				    page, result.getTotalElements());
		}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public VendedorDTO read(@PathVariable("id") Long id) {

		Vendedor Vendedor = repository.findOne(id);
		if (Vendedor == null) {
			throw new NotFoundException(Vendedor.class);
		}
		return convert.toDTO(Vendedor);
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public VendedorDTO create(@Valid @RequestBody VendedorDTO dto) {
		String mensagem = "vendedor.already.inUse";
		Vendedor vendedor = repository.findByName(dto.getName());
		
		if (vendedor != null) {
			throw new WebException(HttpStatus.PRECONDITION_FAILED, mensagem);
		}
		Vendedor entity = convert.toEntity(dto);
		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{ref}", method = RequestMethod.PUT)
	@ResponseBody
	public VendedorDTO update(@PathVariable("ref") Long ref, @Valid @RequestBody VendedorDTO dto) {
		Vendedor entity = repository.findOne(ref);
		Vendedor vendedor = repository.findByName(dto.getName());
		String mensagem = "vendedor.already.inUse";
		
		if (entity == null) {
			throw new NotFoundException(Vendedor.class);
		}
		
		convert.updateEntity(entity, dto, "id", "natures");
		if (vendedor != null) {
			if (entity.getName() == vendedor.getName()){
				throw new WebException(HttpStatus.PRECONDITION_FAILED, mensagem);
			}
		}
		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public VendedorDTO delete(@PathVariable("id") Long id) {

		Vendedor Vendedor = repository.findOne(id);

		if (Vendedor == null) {
			throw new NotFoundException(Vendedor.class);
		}
		
		this.repository.delete(Vendedor);

		return convert.toDTO(Vendedor);
	}

}


