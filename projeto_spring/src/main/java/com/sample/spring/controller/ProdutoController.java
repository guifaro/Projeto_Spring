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

import com.sample.spring.controller.dto.ProdutoDTO;
import com.sample.spring.domain.TipoProduto;
import com.sample.spring.domain.Produto;
import com.sample.spring.exception.NotFoundException;
import com.sample.spring.repository.TipoProdutoRepository;
import com.sample.spring.repository.ProdutoRepository;
import com.sample.spring.security.Roles;
import com.sample.spring.utils.MapperUtils;
import com.sample.spring.utils.SQLLikeUtils;

@RestController
@RequestMapping("/produto")
@Secured(Roles.ROLE_ADMIN)

public class ProdutoController {
	private MapperUtils<Produto, ProdutoDTO> convert = new MapperUtils<Produto, ProdutoDTO>(Produto.class, ProdutoDTO.class);
	
	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private TipoProdutoRepository tipoprodutoRepository;
	
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	public Page<ProdutoDTO> list(@PageableDefault(page = 0, size = 50, sort = "name") Pageable page,
	    @RequestParam(value = "search", required = false) String search) {

		Page<Produto> result;
		if (StringUtils.hasText(search)) {
			result = repository.search(SQLLikeUtils.like(search), page);
		} else {
			result = repository.findAll(page);
		}

		return new PageImpl<ProdutoDTO>(result.getContent().stream().map(c -> convert.toDTO(c)).collect(Collectors.toList()),
		    page, result.getTotalElements());
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{ref}", method = RequestMethod.GET)
	public ProdutoDTO read(@PathVariable("ref") Long ref) {

		Produto entity = repository.findByIdWithTipoProduto(ref);

		if (entity == null) {
			throw new NotFoundException(Produto.class);
		}

		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProdutoDTO create(@Valid @RequestBody ProdutoDTO dto) {

		TipoProduto tipoproduto = tipoprodutoRepository.findOne(dto.getTipoproduto().getId());
		if (tipoproduto == null) {
			throw new NotFoundException(TipoProduto.class);
		}

		Produto entity = convert.toEntity(dto);
		entity.setTipoproduto(tipoproduto);

		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{ref}", method = RequestMethod.PUT)
	public ProdutoDTO update(@PathVariable("ref") Long ref, @Valid @RequestBody ProdutoDTO dto) {

		TipoProduto tipoproduto = tipoprodutoRepository.findOne(dto.getTipoproduto().getId());
		if (tipoproduto == null) {
			throw new NotFoundException(TipoProduto.class);
		}

		Produto entity = repository.findOne(ref);
		if (entity == null) {
			throw new NotFoundException(Produto.class);
		}

		convert.updateEntity(entity, dto, "id", "tipoproduto");
		entity.setTipoproduto(tipoproduto);

		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{ref}", method = RequestMethod.DELETE)
	public ProdutoDTO delete(@PathVariable("ref") Long ref) {

		Produto entity = repository.findOne(ref);
		if (entity == null) {
			throw new NotFoundException(Produto.class);
		}

		this.repository.delete(entity);

		return convert.toDTO(entity);
	}
}
