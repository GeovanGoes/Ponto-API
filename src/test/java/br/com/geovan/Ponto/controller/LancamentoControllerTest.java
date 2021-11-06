package br.com.geovan.Ponto.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import br.com.geovan.Ponto.exception.EmptyResultException;
import br.com.geovan.Ponto.service.LancamentoService;
import br.com.geovan.Ponto.to.Dia;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;
import io.restassured.http.ContentType;


@WebMvcTest
public class LancamentoControllerTest {

	private static final String CONTROLLER_PATH = "/lancamentos";

	@Autowired
	private LancamentoController controller;
	
	@MockBean
	private LancamentoService service;
	
	@BeforeEach
	private void setup () {
		standaloneSetup(this.controller);
	}
	
	/**Testes sobre o método listar*/
	
	@Test
	public void deveRetornarSucesso_QuandoListarOsLancamentos() throws EmptyResultException {
		when(this.service.listar()).thenReturn(Arrays.asList(new Dia(LocalDate.now(), Arrays.asList(LocalTime.now()))));
		
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarSucesso_QuandoListarOsLancamentosMesmoComEmptyException() throws EmptyResultException {
		when(this.service.listar()).thenThrow(new EmptyResultException());
		
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarFalha_QuandoListarOsLancamentos() throws EmptyResultException {
		when(this.service.listar()).thenThrow(new NullPointerException());
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	
	/**Testes do método inserir**/
	
	@Test
	public void deveRetornarSucesso_QuandoInserirUmLancamento() {
		ResultBaseFactoryTO resultBaseFactoryTO = new ResultBaseFactoryTO();
		resultBaseFactoryTO.setSuccess(new HashMap<>());
		
		LocalDateTime now = LocalDateTime.now();
		when(this.service.inserir(now)).thenReturn(resultBaseFactoryTO);
		Map<String, Date> data = new HashMap<>();
		data.put("dataHoraLancamento", new Date());
		
		given()
			.accept(ContentType.JSON)
		.when()
			.post(CONTROLLER_PATH, data)
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
}
