package com.sample.spring.repository;

import com.sample.spring.domain.Empresa;
import com.sample.spring.domain.Produto;
import com.sample.spring.domain.EmpresaProduto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmpresaProdutoRepository extends JpaRepository<EmpresaProduto, Long>{
	
	@Query("select ep from EmpresaProduto ep left join ep.empresa e left join ep.produto p"
			+ " where UPPER(e.name) like UPPER(?1)"
			+ "or UPPER(e.razao) like UPPER(?1)"
			+ "or UPPER(p.name) like UPPER(?1)")
	Page<EmpresaProduto> search(String value, Pageable page);
	
	@Query("select e from EmpresaProduto e left join fetch e.empresa v where e.id = ?1")
	EmpresaProduto findByIdWithEmpresa(Long id);
	
	@Query("select e from EmpresaProduto e left join fetch e.produto v where e.id = ?1")
	EmpresaProduto findByIdWithProduto(Long id);
	
	@Query("select e from EmpresaProduto e left join fetch e.user v where e.id = ?1")
	EmpresaProduto findByIdWithUser(Long id);
	
	Long countByEmpresa(Empresa empresa);
	Long countByProduto(Produto produto);
	
}
