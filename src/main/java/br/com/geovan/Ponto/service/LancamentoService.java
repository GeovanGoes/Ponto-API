package br.com.geovan.Ponto.service;

import br.com.geovan.Ponto.exception.EmptyResultException;
import br.com.geovan.Ponto.model.Lancamento;
import br.com.geovan.Ponto.repository.LancamentoRepository;
import br.com.geovan.Ponto.to.Dia;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService 
{
	Logger _log = LoggerFactory.getLogger(LancamentoService.class);
	
	@Autowired
	LancamentoRepository repository;

	public ResultBaseFactoryTO inserir(LocalDateTime dataHora)
	{
		ResultBaseFactoryTO response = new ResultBaseFactoryTO();
		if (dataHora != null)
		{
			_log.info("inserindo lancamento...");
			_log.info(dataHora.toString());			
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
	 * @throws EmptyResultException 
	 */
	public List<Dia> listar() throws EmptyResultException
	{
		Iterable<Lancamento> todosLancamentos = repository.findAllByOrderByDataHoraLancamento();
		
		if (todosLancamentos != null)
		{
			Map<LocalDate, List<LocalTime>> lancs = new HashMap<>();

			todosLancamentos.forEach(lancamento -> {
				LocalDate date = lancamento.getDataHoraLancamento().toLocalDate();
				if(lancs.containsKey(date))
				{
					List<LocalTime> list = lancs.get(date);
					list.add(lancamento.getDataHoraLancamento().toLocalTime());
					lancs.put(date, list);
				}
				else
				{
					List<LocalTime> horas = new ArrayList<>();
					horas.add(lancamento.getDataHoraLancamento().toLocalTime());
					lancs.put(date, horas);
				}
			});
			
			Set<Dia> dias = new HashSet<>();
			
			lancs.keySet().forEach(key -> {
				dias.add(new Dia(key, lancs.get(key)));
			});
			
			List<Dia> list = dias.stream().collect(Collectors.toList());
			Collections.sort(list);
			return list;
		}
		else
			throw new EmptyResultException();
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
