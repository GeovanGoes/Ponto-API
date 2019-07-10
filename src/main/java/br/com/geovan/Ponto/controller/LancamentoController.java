package br.com.geovan.Ponto.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.geovan.Ponto.service.LancamentoService;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController 
{
	@Autowired
	LancamentoService service;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResultBaseFactoryTO inserir(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamento)
	{
		return service.inserir(convertDateToLocalDateTime(dataHoraLancamento));
	}

	/**
	 * @param dataHoraLancamento
	 * @return
	 */
	private LocalDateTime convertDateToLocalDateTime(Date dataHoraLancamento) 
	{
		return new DateUtil().dateToLocalDateTime(dataHoraLancamento);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResultBaseFactoryTO listar()
	{
		return service.listar();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResultBaseFactoryTO atualizar(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamentoAntigo, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamentoNovo)
	{
		return service.atualizar(convertDateToLocalDateTime(dataHoraLancamentoAntigo), convertDateToLocalDateTime(dataHoraLancamentoNovo));
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResultBaseFactoryTO deletar (@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamento)
	{
		return service.delete(convertDateToLocalDateTime(dataHoraLancamento));
	}
}
