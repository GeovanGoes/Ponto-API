package br.com.geovan.Ponto.controller;

import static br.com.geovan.Ponto.util.DateUtil.DEFAULT_PATTERN_FOR_DATE_TIME;

import br.com.geovan.Ponto.exception.EmptyResultException;
import br.com.geovan.Ponto.service.LancamentoService;
import br.com.geovan.Ponto.to.Dia;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/lancamentos")
public class LancamentoController 
{
	@Autowired
	LancamentoService service;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResultBaseFactoryTO inserir(@DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamento)
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
	public ResponseEntity<?> listar()
	{
		try {
			List<Dia> list = service.listar();
			return ResponseEntity.ok(list);
		} catch (EmptyResultException ere) {
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResultBaseFactoryTO atualizar(@DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamentoAntigo, @DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamentoNovo)
	{
		return service.atualizar(convertDateToLocalDateTime(dataHoraLancamentoAntigo), convertDateToLocalDateTime(dataHoraLancamentoNovo));
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResultBaseFactoryTO deletar (@DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamento)
	{
		return service.delete(convertDateToLocalDateTime(dataHoraLancamento));
	}
}
