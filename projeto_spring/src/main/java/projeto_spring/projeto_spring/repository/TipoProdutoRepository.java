package projeto_spring.projeto_spring.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import projeto_spring.projeto_spring.domain.TipoProduto;


public interface  TipoProdutoRepository extends JpaRepository <TipoProduto, Long>{
	@Query("select c from TipoProduto c where UPPER(c.name) like UPPER(?1) or UPPER(c.pagamento) like UPPER(?1) or UPPER(c.unico) like UPPER(?1)")
	Page<TipoProduto> search(String value, Pageable page);
}
