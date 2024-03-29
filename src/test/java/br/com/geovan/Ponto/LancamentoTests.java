/**
 * 
 */
package br.com.geovan.Ponto;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.geovan.Ponto.controller.LancamentoController;
import br.com.geovan.Ponto.model.Lancamento;
import br.com.geovan.Ponto.util.DateUtil;

/**
 * @author geovan.goes
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class LancamentoTests
{
	@Autowired
	LancamentoController lancamentosController;
	
	DateUtil dateUtil;
	
	@BeforeEach
	public void antes()
	{
		dateUtil = new DateUtil();
	}
	
	@Test
	public void deveInserirComSucesso()
	{
		ResponseEntity<?> response = inserirLancamentoDeAgora();
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void deveInserirComFalha()
	{
		ResponseEntity<?> responseEntity = lancamentosController.inserir(null, null);
		Assertions.assertNotNull(responseEntity);
		Assertions.assertNotEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		//assertTrue(obterListaDeErrosDoResponse(responseEntity).containsKey("Data invalida"));
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
		ResponseEntity<?> inserir = lancamentosController.inserir(dateUtil.localDateTimeToDate(dataHoraLancamento), "geovansilvagoes@gmail.com");
		return inserir;
	}
	
	@Test
	public void deveListarInseridos()
	{	
		inserirLancamentoDeAgora();
		ResponseEntity<?> responseListar = lancamentosController.listar("geovansilvagoes@gmail.com");
		Assertions.assertNotNull(responseListar.getBody());
		Assertions.assertTrue(responseListar.getStatusCode().equals(HttpStatus.OK));
		
		List<Lancamento> lista = (ArrayList<Lancamento>) responseListar.getBody();
		Assertions.assertNotNull(lista);
		
		Assertions.assertNotEquals(0, lista.size());
		
	}

	/**
	 * @return
	 */
	private LocalDateTime asserttionParaInsercaoComDataAgora()
	{
		LocalDateTime dataHoraLancamento = LocalDateTime.now();
		ResponseEntity<?> response = lancamentosController.inserir(dateUtil.localDateTimeToDate(dataHoraLancamento), "geovansilvagoes@gmail.com");
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		return dataHoraLancamento;
	}
	
	@Test
	public void deveDeletarComSucesso()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> deletarResponse = lancamentosController.deletar(dateUtil.localDateTimeToDate(now), "geovansilvagoes@gmail.com");
		
		Assertions.assertNotNull(deletarResponse);	
	}
	
	@Test
	public void deveDeletarComFalha()
	{
		asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> deletarResponse = lancamentosController.deletar(null, null);
		
		Assertions.assertNotNull(deletarResponse);
		//assertFalse(deletarResponse.isSuccess());		
	}
	
	
	@Test
	public void deveAtualizarComSucesso()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> atualizarResponse = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now), dateUtil.localDateTimeToDate(now.plusMinutes(1)), "geovansilvagoes@gmail.com");
		
		Assertions.assertNotNull(atualizarResponse);
		Assertions.assertEquals(HttpStatus.OK, atualizarResponse.getStatusCode());
	}
	
	@Test
	public void deveAtualizarComFalha()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> response = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now), null, null);
		
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		//assertTrue(obterListaDeErrosDoResponse(response).containsKey("parametros invalidos"));
	}
	
	@Test
	public void deveAtualizarComFalha_LancamentoInexistente()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResponseEntity<?> response = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now.plusYears(100)), dateUtil.localDateTimeToDate(now), "geovansilvagoes@gmail.com");
		
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assertions.assertTrue(obterListaDeErrosDoResponse(response).containsKey("nao existe um lancamento com a data/hora informada"));
	}
}
