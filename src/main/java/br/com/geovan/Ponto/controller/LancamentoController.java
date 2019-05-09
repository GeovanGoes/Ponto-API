package br.com.geovan.Ponto.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

	@RequestMapping(method = RequestMethod.POST)
	public ResultBaseFactoryTO inserir(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamento)
	{
		return service.inserir(dataHoraLancamento);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResultBaseFactoryTO listar()
	{
		return service.listar();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResultBaseFactoryTO atualizar(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamentoAntigo, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date dataHoraLancamentoNovo)
	{
		return service.atualizar(dataHoraLancamentoAntigo, dataHoraLancamentoNovo);
	}
}
