package br.com.geovan.Ponto.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import br.com.geovan.Ponto.exception.EmptyResultException;
import br.com.geovan.Ponto.model.Usuario;
import br.com.geovan.Ponto.service.LancamentoService;
import br.com.geovan.Ponto.service.UsuarioService;
import br.com.geovan.Ponto.to.Dia;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import io.restassured.http.ContentType;


@WebMvcTest
public class LancamentoControllerTest {

	private static final String VALID_EMAIL = "geovansilvagoes@gmail.com";

	private static final String CONTROLLER_PATH = "/lancamentos";

	@Autowired
	private LancamentoController controller;
	
	@MockBean
	private LancamentoService service;
	
	@MockBean
	private UsuarioService usuarioService;
	
	@BeforeEach
	private void setup () {
		standaloneSetup(this.controller);
	}
	
	/**Testes sobre o metodo listar*/
	
	@Test
	public void deveRetornarSucesso_QuandoListarOsLancamentos() throws EmptyResultException {
		when(this.service.listar(getUsuarioMock())).thenReturn(Arrays.asList(new Dia(LocalDate.now(), Arrays.asList(LocalTime.now()))));
		
		
		given()
			.accept(ContentType.JSON)
			.param("email", VALID_EMAIL)
		.when()
			.get(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	private Usuario getUsuarioMock() {
		Usuario usuario = new Usuario(999L, VALID_EMAIL, "abc", "Geovan");
		when(this.usuarioService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(usuario));
		return usuario;
	}
	
	@Test
	public void deveRetornarSucesso_QuandoListarOsLancamentosMesmoComEmptyException() throws EmptyResultException {
		when(this.service.listar(getUsuarioMock())).thenThrow(new EmptyResultException());
		
		
		given()
			.accept(ContentType.JSON)
			.param("email", VALID_EMAIL)
		.when()
			.get(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarFalha_QuandoListarOsLancamentos() throws EmptyResultException {
		when(this.service.listar(getUsuarioMock())).thenThrow(new NullPointerException());
		
		given()
			.accept(ContentType.JSON)
			.param("email", VALID_EMAIL)
		.when()
			.get(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	
	/**Testes do metodo inserir**/
	
	@Test
	public void deveRetornarSucesso_QuandoInserirUmLancamento() {
		ResultBaseFactoryTO resultBaseFactoryTO = getBaseFactorySuccess();
		
		when(this.service.inserir(LocalDateTime.of(LocalDate.of(2021, 2, 22), LocalTime.of(10, 30)), getUsuarioMock())).thenReturn(resultBaseFactoryTO);
		
		given()
			.accept(ContentType.JSON)
			.param("dataHoraLancamento", "2021-02-22 10:30" )
			.param("email", VALID_EMAIL)
		.when()
			.post(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarFalha_QuandoInserirUmLancamentoInvalido() {
		when(this.service.inserir(LocalDateTime.of(LocalDate.of(2021, 2, 22), LocalTime.of(10, 30)), getUsuarioMock())).thenReturn(new ResultBaseFactoryTO());
		
		given()
			.accept(ContentType.JSON)
			.param("dataHoraLancamento", "" )
			.param("email", VALID_EMAIL)
		.when()
			.post(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void deveRetornarFalha_QuandoNaoInserirUmLancamento() {
		when(this.service.inserir(LocalDateTime.of(LocalDate.of(2021, 2, 22), LocalTime.of(10, 30)), getUsuarioMock())).thenReturn(new ResultBaseFactoryTO());
		
		given()
			.accept(ContentType.JSON)
			.param("dataHoraLancamento", "2021-02-22 10:30" )
			.param("email", VALID_EMAIL)
		.when()
			.post(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	/**Testes do metodo atualizar**/
	
	@Test
	public void deveRetornarSucesso_QuandoUmLancamentoForAtualizado() {
		when(this.service.atualizar(LocalDateTime.of(LocalDate.of(2021, 2, 22), LocalTime.of(10, 30)), LocalDateTime.of(LocalDate.of(2021, 2, 22), LocalTime.of(10, 31)), getUsuarioMock())).thenReturn(getBaseFactorySuccess());
		
		given()
			.accept(ContentType.JSON)
			.param("dataHoraLancamentoAntigo", "2021-02-22 10:30" )
			.param("dataHoraLancamentoNovo", "2021-02-22 10:31")
			.param("email", VALID_EMAIL)
		.when()
			.put(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarFalha_QuandoUmLancamentoNaoForAtualizado() {
		when(this.service.atualizar(LocalDateTime.of(LocalDate.of(2021, 2, 22), LocalTime.of(10, 30)), LocalDateTime.of(LocalDate.of(2021, 2, 22), LocalTime.of(10, 30)), getUsuarioMock())).thenReturn(new ResultBaseFactoryTO());
		
		given()
			.accept(ContentType.JSON)
			.param("dataHoraLancamentoAntigo", "2021-02-22 10:30" )
			.param("dataHoraLancamentoNovo", "2021-02-22 10:30")
			.param("email", VALID_EMAIL)
		.when()
			.put(CONTROLLER_PATH)
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	private ResultBaseFactoryTO getBaseFactorySuccess() {
		ResultBaseFactoryTO resultBaseFactoryTO = new ResultBaseFactoryTO();
		resultBaseFactoryTO.setSuccess(new HashMap<>());
		return resultBaseFactoryTO;
	}
	
}
