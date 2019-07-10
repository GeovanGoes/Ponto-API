/**
 * 
 */
package br.com.geovan.Ponto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import br.com.geovan.Ponto.controller.LancamentoController;
import br.com.geovan.Ponto.model.Lancamento;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;

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
import org.springframework.test.context.junit4.SpringRunner;

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
		ResultBaseFactoryTO response = inserirLancamentoDeAgora();
		assertNotNull(response);
		assertTrue(response.isSuccess());
	}

	@Test
	public void deveInserirComFalha()
	{
		ResultBaseFactoryTO response = lancamentosController.inserir(null);
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertTrue(response.getErrorMessages().containsKey("Data inválida"));
	}
	
	/**
	 * @return
	 */
	private ResultBaseFactoryTO inserirLancamentoDeAgora()
	{
		LocalDateTime dataHoraLancamento = LocalDateTime.now();
		ResultBaseFactoryTO response = lancamentosController.inserir(dateUtil.localDateTimeToDate(dataHoraLancamento));
		return response;
	}
	
	@Test
	public void deveListarInseridos()
	{
		LocalDateTime dataHoraLancamento = asserttionParaInsercaoComDataAgora();
		
		ResultBaseFactoryTO responseListar = lancamentosController.listar();
		assertNotNull(responseListar);
		assertTrue(responseListar.isSuccess());
		
		Map<String, Object> result = responseListar.getResult();
		assertNotNull(result);
		assertTrue(result.containsKey("lancamentos"));
		
		Object objectLancamentos = result.get("lancamentos");
		assertNotNull(objectLancamentos);
		
		List<Lancamento> lista = (ArrayList<Lancamento>) objectLancamentos;
		assertNotNull(lista);
		
		List<Lancamento> collect = lista.stream().filter(lancamento -> lancamento.getDataHoraLancamento().equals(dataHoraLancamento)).collect(Collectors.toList());
		
		assertEquals(1, collect.size());
	}

	/**
	 * @return
	 */
	private LocalDateTime asserttionParaInsercaoComDataAgora()
	{
		LocalDateTime dataHoraLancamento = LocalDateTime.now();
		ResultBaseFactoryTO responseInserir = lancamentosController.inserir(dateUtil.localDateTimeToDate(dataHoraLancamento));
		
		assertNotNull(responseInserir);
		assertTrue(responseInserir.isSuccess());
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
		
		ResultBaseFactoryTO atualizarResponse = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now), dateUtil.localDateTimeToDate(now.plusMinutes(1)));
		
		assertNotNull(atualizarResponse);
		assertTrue(atualizarResponse.isSuccess());
	}
	
	@Test
	public void deveAtualizarComFalha()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResultBaseFactoryTO atualizarResponse = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now), null);
		
		assertNotNull(atualizarResponse);
		assertFalse(atualizarResponse.isSuccess());
		assertTrue(atualizarResponse.getErrorMessages().containsKey("parametros inválidos"));
	}
	
	@Test
	public void deveAtualizarComFalhaLancamentoInexistente()
	{
		LocalDateTime now = asserttionParaInsercaoComDataAgora();
		
		ResultBaseFactoryTO atualizarResponse = lancamentosController.atualizar(dateUtil.localDateTimeToDate(now.plusYears(100)), dateUtil.localDateTimeToDate(now));
		
		assertNotNull(atualizarResponse);
		assertFalse(atualizarResponse.isSuccess());
		assertTrue(atualizarResponse.getErrorMessages().containsKey("não existe um lancamento com a data/hora informada"));
	}
}
