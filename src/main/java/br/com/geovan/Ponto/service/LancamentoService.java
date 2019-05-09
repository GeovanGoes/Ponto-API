package br.com.geovan.Ponto.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.geovan.Ponto.model.Lancamento;
import br.com.geovan.Ponto.repository.LancamentoRepository;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;

@Service
public class LancamentoService 
{
	@Autowired
	LancamentoRepository repository;

	public ResultBaseFactoryTO inserir(Date dataHora)
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		if (dataHora != null)
		{
			System.out.println("inserindo lancamento...");
			System.out.println(dataHora);			
			Lancamento saved = repository.save(new Lancamento(dataHora));
			if (saved != null)
				response.setSuccess(new HashMap<String, Object>());
		}
		else
			response.addErrorMessage("Data inválida", "Data inválida");
		return response;
	}
	
	/***
	 * 
	 * @return
	 */
	public ResultBaseFactoryTO listar()
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		Iterable<Lancamento> todosLancamentos = repository.findAll();
		
		if (todosLancamentos != null)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lancamentos", todosLancamentos);
			response.setSuccess(map);	
		}
		else
			response.addErrorMessage("não foi possível obter a lista de lancamentos", "não foi possível obter a lista de lancamentos");
		return response;
	}
	
	/***
	 * 
	 * @param dataHoraLancamentoAntiga
	 * @param dataHoraLancamentoNova
	 * @return
	 */
	public ResultBaseFactoryTO atualizar(Date dataHoraLancamentoAntiga, Date dataHoraLancamentoNova)
	{
		System.out.println(new Date());
		System.out.println(dataHoraLancamentoAntiga);
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		Lancamento lancamentoByDataHoraLancamento = repository.getLancamentoByDataHoraLancamento(dataHoraLancamentoAntiga);
		if (lancamentoByDataHoraLancamento != null)
		{
			lancamentoByDataHoraLancamento.setDataHoraLancamento(dataHoraLancamentoNova);
			Lancamento updated = repository.save(lancamentoByDataHoraLancamento);
			if (updated != null)
			{
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("updated", updated);
				response.setSuccess(map);				
			}
			else
				response.addErrorMessage("não foi possível atualizar o lancamento", "não foi possível atualizar o lancamento");
		}
		else
			response.addErrorMessage("não existe um lancamento com a data/hora informada", "não existe um lancamento com a data/hora informada");
		return response;
	}
	
}
