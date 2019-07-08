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

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController 
{
	@Autowired
	LancamentoService service;

	@ModelAttribute
	LocalDateTime initLocalDateTime()
	{
		return LocalDateTime.now();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResultBaseFactoryTO inserir(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamento)
	{
		return service.inserir(LocalDateTime.ofInstant(dataHoraLancamento.toInstant(), ZoneId.systemDefault()));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResultBaseFactoryTO listar()
	{
		return service.listar();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResultBaseFactoryTO atualizar(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") @ModelAttribute LocalDateTime dataHoraLancamentoAntigo, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") @ModelAttribute LocalDateTime dataHoraLancamentoNovo)
	{
		return service.atualizar(dataHoraLancamentoAntigo, dataHoraLancamentoNovo);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResultBaseFactoryTO deletar (@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") @ModelAttribute LocalDateTime dataHoraLancamento)
	{
		return service.delete(dataHoraLancamento);
	}
}
