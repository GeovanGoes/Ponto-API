package br.com.geovan.Ponto.controller;

import static br.com.geovan.Ponto.util.DateUtil.DEFAULT_PATTERN_FOR_DATE_TIME;

import br.com.geovan.Ponto.exception.EmptyResultException;
import br.com.geovan.Ponto.model.Usuario;
import br.com.geovan.Ponto.service.LancamentoService;
import br.com.geovan.Ponto.service.UsuarioService;
import br.com.geovan.Ponto.to.Dia;
import br.com.geovan.Ponto.to.ResultBaseFactoryTO;
import br.com.geovan.Ponto.util.DateUtil;
import br.com.geovan.Ponto.util.ErrorsEnum;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<?> inserir(@RequestParam(required = true) @DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamento,
			@RequestParam(required = true) String email)
	{		
		Optional<Usuario> findByEmail = usuarioService.findByEmail(email);
		if (!findByEmail.isPresent())
			return ResponseEntity.badRequest().body(ResultBaseFactoryTO.getReponseWithSingleError(ErrorsEnum.USUARIO_NAO_INDENTIFICADO));
		
		Usuario usuario = findByEmail.get();
		try {
			ResultBaseFactoryTO resultBaseFactoryTO = service.inserir(convertDateToLocalDateTime(dataHoraLancamento), usuario);
			if (resultBaseFactoryTO.isSuccess())
				return ResponseEntity.created(null).build();
			else
				return ResponseEntity.badRequest().body(resultBaseFactoryTO.getErrorMessages());			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}	
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
	public ResponseEntity<?> listar(@RequestParam(required = true) String email)
	{
		try {
			Optional<Usuario> findByEmail = usuarioService.findByEmail(email);
			if (!findByEmail.isPresent())
				return ResponseEntity.badRequest().body(ResultBaseFactoryTO.getReponseWithSingleError(ErrorsEnum.USUARIO_NAO_INDENTIFICADO));
			
			Usuario usuario = findByEmail.get();
			List<Dia> list = service.listar(usuario);
			return ResponseEntity.ok(list);
		} catch (EmptyResultException ere) {
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping
	public ResponseEntity<?> atualizar(@DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamentoAntigo, @DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamentoNovo, @RequestParam(required = true) String email)
	{
		Optional<Usuario> findByEmail = usuarioService.findByEmail(email);
		if (!findByEmail.isPresent())
			return ResponseEntity.badRequest().body(ResultBaseFactoryTO.getReponseWithSingleError(ErrorsEnum.USUARIO_NAO_INDENTIFICADO));
		
		Usuario usuario = findByEmail.get();
		
		ResultBaseFactoryTO atualizar = service.atualizar(convertDateToLocalDateTime(dataHoraLancamentoAntigo), convertDateToLocalDateTime(dataHoraLancamentoNovo), usuario);
		
		if (atualizar.isSuccess())
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.badRequest().body(atualizar.getErrorMessages());
	}
	
	@DeleteMapping
	public ResponseEntity<?> deletar (@DateTimeFormat(pattern=DEFAULT_PATTERN_FOR_DATE_TIME) Date dataHoraLancamento, @RequestParam(required = true) String email)
	{
		Optional<Usuario> findByEmail = usuarioService.findByEmail(email);
		if (!findByEmail.isPresent())
			return ResponseEntity.badRequest().body(ResultBaseFactoryTO.getReponseWithSingleError(ErrorsEnum.USUARIO_NAO_INDENTIFICADO));
		
		Usuario usuario = findByEmail.get();
		ResultBaseFactoryTO deleteResponse = service.delete(convertDateToLocalDateTime(dataHoraLancamento), usuario);
		
		return ResponseEntity.ok(deleteResponse);
	}
}
