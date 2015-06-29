package com.sample.spring.repository;

import com.sample.spring.domain.Empresa;
import com.sample.spring.domain.Vendedor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
	
	@Query("select e from Empresa e left join e.vendedor v where UPPER(v.name) like UPPER(?1)"
			+ "or UPPER(e.razao) like UPPER(?1)"
			+ "or UPPER(e.name) like UPPER(?1)")
	Page<Empresa> search(String value, Pageable page);
	
	@Query("select e from Empresa e left join e.vendedor v where e.id = ?1")
	Empresa findByIdWithVendedor(Long id);
	
	
     Long countByVendedor(Vendedor empresa);

}
