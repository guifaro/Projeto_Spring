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

import com.sample.spring.controller.dto.TipoProdutoDTO;
import com.sample.spring.domain.TipoProduto;
import com.sample.spring.exception.NotFoundException;
import com.sample.spring.repository.TipoProdutoRepository;
import com.sample.spring.security.Roles;
import com.sample.spring.utils.MapperUtils;
import com.sample.spring.utils.SQLLikeUtils;

@RestController
@RequestMapping("/tipoproduto")
@Secured(Roles.ROLE_ADMIN)

public class TipoProdutoController {
	private MapperUtils<TipoProduto, TipoProdutoDTO> convert = new MapperUtils<TipoProduto, TipoProdutoDTO>(TipoProduto.class, TipoProdutoDTO.class);
	
	@Autowired
	private TipoProdutoRepository repository;

	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	public Page<TipoProdutoDTO> list(@PageableDefault(page = 0, size = 50, sort = "name") Pageable page,
		    @RequestParam(value = "search", required = false) String search) {

			Page<TipoProduto> result;

			if (StringUtils.hasText(search)) {
				result = repository.search(SQLLikeUtils.like(search), page);
			} else {
				result = repository.findAll(page);
			}

			return new PageImpl<TipoProdutoDTO>(result.getContent().stream().map(c -> convert.toDTO(c)).collect(Collectors.toList()),
				    page, result.getTotalElements());
		}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TipoProdutoDTO read(@PathVariable("id") Long id) {

		TipoProduto tipoproduto = repository.findOne(id);
		if (tipoproduto == null) {
			throw new NotFoundException(TipoProduto.class);
		}
		return convert.toDTO(tipoproduto);
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public TipoProdutoDTO create(@Valid @RequestBody TipoProdutoDTO dto) {

		TipoProduto entity = convert.toEntity(dto);
		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{ref}", method = RequestMethod.PUT)
	@ResponseBody
	public TipoProdutoDTO update(@PathVariable("ref") Long ref, @Valid @RequestBody TipoProdutoDTO dto) {
		TipoProduto entity = repository.findOne(ref);
		if (entity == null) {
			throw new NotFoundException(TipoProduto.class);
		}
		convert.updateEntity(entity, dto, "id", "natures");
		entity = repository.save(entity);
		return convert.toDTO(entity);
	}

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public TipoProdutoDTO delete(@PathVariable("id") Long id) {

		TipoProduto tipoproduto = repository.findOne(id);

		if (tipoproduto == null) {
			throw new NotFoundException(TipoProduto.class);
		}
		
		//if (hotelRepository.countByCity(city) > 0) {
			//throw new WebException(HttpStatus.PRECONDITION_FAILED, "city.hasHotel");
		//}

		this.repository.delete(tipoproduto);

		return convert.toDTO(tipoproduto);
	}

}
