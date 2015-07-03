package com.sample.spring.repository;

import com.sample.spring.domain.Produto;
import com.sample.spring.domain.TipoProduto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query("select p from Produto p left join p.tipoproduto t where UPPER(t.name) like UPPER(?1)"
			+ "or UPPER(t.pagamento) like UPPER(?1)"
			+ "or UPPER(p.name) like UPPER(?1)")
	Page<Produto> search(String value, Pageable page);
	
	@Query("select p from Produto p left join fetch p.tipoproduto t where p.id = ?1")
	Produto findByIdWithTipoProduto(Long id);
	
	
     Long countByTipoproduto(TipoProduto produto);
}


