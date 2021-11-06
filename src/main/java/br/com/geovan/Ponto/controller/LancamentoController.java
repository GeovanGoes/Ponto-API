package br.com.geovan.Ponto.controller;

import static br.com.geovan.Ponto.util.DateUtil.DEFAULT_PATTERN_FOR_DATE_TIME;

import br.com.geovan.Ponto.exception.EmptyResultException;
import br.com.geovan.Ponto.service.LancamentoService;
import br.com.geovan.Ponto.to.Dia;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/lancamentos")
public class LancamentoController 
{
	@Autowired
	LancamentoService service;
	
	@PostMapping
	public ResponseEntity<?> inserir(@RequestParam @DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamento)
	{
		if (dataHoraLancamento == null) {
			ResultBaseFactoryTO to = new ResultBaseFactoryTO();
			to.addErrorMessage("Data invalida", "Data invalida");
			return ResponseEntity.badRequest().body(to.getErrorMessages());
		}
		ResultBaseFactoryTO resultBaseFactoryTO = service.inserir(convertDateToLocalDateTime(dataHoraLancamento));
		if (resultBaseFactoryTO.isSuccess())
			return ResponseEntity.created(null).build();
		else
			return ResponseEntity.badRequest().body(resultBaseFactoryTO.getErrorMessages());
	}

	/**
	 * @param dataHoraLancamento
	 * @return
	 */
	private LocalDateTime convertDateToLocalDateTime(Date dataHoraLancamento) 
	{
		return new DateUtil().dateToLocalDateTime(dataHoraLancamento);
	}
	
	@GetMapping
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
	
	@PutMapping
	public ResponseEntity<?> atualizar(@DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamentoAntigo, @DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamentoNovo)
	{
		ResultBaseFactoryTO atualizar = service.atualizar(convertDateToLocalDateTime(dataHoraLancamentoAntigo), convertDateToLocalDateTime(dataHoraLancamentoNovo));
		
		if (atualizar.isSuccess())
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.badRequest().body(atualizar.getErrorMessages());
	}
	
	@DeleteMapping
	public ResultBaseFactoryTO deletar (@DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamento)
	{
		return service.delete(convertDateToLocalDateTime(dataHoraLancamento));
	}
}
