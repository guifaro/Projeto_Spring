package com.sample.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sample.spring.domain.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
	
	@Query("select v from Vendedor v where UPPER(c.name) like UPPER(?1)")
	Page<Vendedor> search(String value, Pageable page);

	Vendedor findByName(String name);
	
}
