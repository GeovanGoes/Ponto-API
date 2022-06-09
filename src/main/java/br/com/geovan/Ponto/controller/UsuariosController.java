package br.com.geovan.Ponto.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.geovan.Ponto.model.Usuario;
import br.com.geovan.Ponto.service.UsuarioService;
import br.com.geovan.Ponto.to.UsuarioTO;

@CrossOrigin("*")
@RestController
@RequestMapping("/usuarios")
public class UsuariosController 
{
	@Autowired
	private UsuarioService service;
	
	@GetMapping
	public ResponseEntity<?> obterDadosUsuario(@RequestParam(required = true) String email) 
	{
		Optional<Usuario> optional = service.findByEmail(email);
		
		if (optional.isPresent())
			return ResponseEntity.ok(new UsuarioTO(optional.get().getNome()));
		else
			return ResponseEntity.notFound().build();
	}
}
