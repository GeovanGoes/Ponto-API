/**
 * 
 */
package br.com.geovan.Ponto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.geovan.Ponto.controller.LancamentoController;
import br.com.geovan.Ponto.model.Lancamento;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;

/**
 * @author geovan.goes
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LancamentoTests
{
	@Autowired
	LancamentoController lancamentosController;
	
	DateUtil dateUtil;
	
	@Before
	public void antes()
	{
		dateUtil = new DateUtil();
	}
	
	@Test
	public void deveInserirComSucesso()
	{
		ResponseEntity<?> response = inserirLancamentoDeAgora();
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void deveInserirComFalha()
	{
		ResponseEntity<?> responseEntity = lancamentosController.inserir(null);
		assertNotNull(responseEntity);
		assertNotEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertTrue(obterListaDeErrosDoResponse(responseEntity).containsKey("Data invalida"));
	}

	private Map<String, String> obterListaDeErrosDoResponse(ResponseEntity<?> responseEntity) {
		Map<String, String> mapped = (Map<String, String>)responseEntity.getBody();
		return mapped;
	}
	
	/**
	 * @return
	 */
	private ResponseEntity<?> inserirLancamentoDeAgora()
	{
		LocalDateTime dataHoraLancamento = LocalDateTime.now();
		ResponseEntity<?> inserir = lancamentosController.inserir(dateUtil.localDateTimeToDate(dataHoraLancamento));
		return inserir;
	}
	
	@Test
	public void deveListarInseridos()
	{	
		inserirLancamentoDeAgora();
		ResponseEntity<?> responseListar = lancamentosController.listar();
		assertNotNull(responseListar.getBody());
		assertTrue(responseListar.getStatusCode().equals(HttpStatus.OK));
		
		List<Lancamento> lista = (ArrayList<Lancamento>) responseListar.getBody();
		assertNotNull(lista);
		
		assertNotEquals(0, lista.size());
		
	}

	/**
	 * @return
	 */
	private LocalDateTime asserttionParaInsercaoComDataAgora()
	{
		LocalDateTime dataHoraLancamento = LocalDateTime.now();
		ResponseEntity<?> response = lancamentosController.inserir(dateUtil.localDateTimeToDate(dataHoraLancamento));
		
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		return dataHoraLancamento;
	}
	
	@Test
	public void deveDeletarComSucesso()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResultBaseFactoryTO deletarResponse = lancamentosController.deletar(dateUtil.localDateTimeToDate(now));
		
		assertNotNull(deletarResponse);
		assertTrue(deletarResponse.isSuccess());		
	}
	
	@Test
	public void deveDeletarComFalha()
	{
		asserttionParaInsercaoComDataAgora();
		
		ResultBaseFactoryTO deletarResponse = lancamentosController.deletar(null);
		
		assertNotNull(deletarResponse);
		assertFalse(deletarResponse.isSuccess());		
	}
	
	
	@Test
	public void deveAtualizarComSucesso()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> atualizarResponse = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now), dateUtil.localDateTimeToDate(now.plusMinutes(1)));
		
		assertNotNull(atualizarResponse);
		assertEquals(HttpStatus.OK, atualizarResponse.getStatusCode());
	}
	
	@Test
	public void deveAtualizarComFalha()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> response = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now), null);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(obterListaDeErrosDoResponse(response).containsKey("parametros invalidos"));
	}
	
	@Test
	public void deveAtualizarComFalha_LancamentoInexistente()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> response = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now.plusYears(100)), dateUtil.localDateTimeToDate(now));
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(obterListaDeErrosDoResponse(response).containsKey("nao existe um lancamento com a data/hora informada"));
	}
}
