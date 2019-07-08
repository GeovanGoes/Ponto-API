package br.com.geovan.Ponto.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public ResultBaseFactoryTO inserir(LocalDateTime dataHora)
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
			response.addErrorMessage("Data invalida", "Data invalida");
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
			List<LocalDateTime> lancamentos = new ArrayList<LocalDateTime>();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lancamentos", todosLancamentos);
			response.setSuccess(map);	
		}
		else
			response.addErrorMessage("nao foi possivel obter a lista de lancamentos", "nao foi possivel obter a lista de lancamentos");
		return response;
	}
	
	/***
	 * 
	 * @param dataHoraLancamentoAntiga
	 * @param dataHoraLancamentoNova
	 * @return
	 */
	public ResultBaseFactoryTO atualizar(LocalDateTime dataHoraLancamentoAntiga, LocalDateTime dataHoraLancamentoNova)
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		
		if (dataHoraLancamentoAntiga != null && dataHoraLancamentoNova != null)
		{
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
					response.addErrorMessage("nao foi possivel atualizar o lancamento", "nao foi possivel atualizar o lancamento");
			}
			else
				response.addErrorMessage("nao existe um lancamento com a data/hora informada", "nao existe um lancamento com a data/hora informada");
		}
		else
			response.addErrorMessage("parametros invalidos", "parametros invalidos");
		
		return response;
	}
	
	/**
	 * @param dataHoraLancamento
	 * @return
	 */
	public ResultBaseFactoryTO delete(LocalDateTime dataHoraLancamento)
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		
		Lancamento lancamentoByDataHoraLancamento = repository.getLancamentoByDataHoraLancamento(dataHoraLancamento);
		
		if (lancamentoByDataHoraLancamento != null)
		{
			repository.delete(lancamentoByDataHoraLancamento);
			response.setSuccess(new HashMap<String, Object>());
		}
		else
			response.addErrorMessage("lancamento nao encontrado", "lancamento nao encontrado");
		
		return response;
	}
	
}
