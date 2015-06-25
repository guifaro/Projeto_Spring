package com.sample.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sample.spring.domain.TipoProduto;

public interface TipoProdutoRepository extends JpaRepository<TipoProduto, Long> {
	
	@Query("select t from TipoProduto t where UPPER(c.name) like UPPER(?1) or UPPER(c.pagamento) like UPPER(?1)")
	Page<TipoProduto> search(String value, Pageable page);
}
