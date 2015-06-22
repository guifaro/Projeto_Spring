package projeto_spring.projeto_spring.controller;

import projeto_spring.projeto_spring.repository.TipoProdutoRepository;
import projeto_spring.projeto_spring.domain.TipoProduto;
import projeto_spring.projeto_spring.security.Roles;
import projeto_spring.projeto_spring.controller.dto.TipoProdutoDTO;
import projeto_spring.projeto_spring.Utils.MapperUtils;
import projeto_spring.projeto_spring.Utils.SQLLikeUtils;
import projeto_spring.projeto_spring.exception.*;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TipoProdutoControllerTest {

}
