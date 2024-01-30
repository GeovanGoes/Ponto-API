package br.com.geovan.Ponto.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.geovan.Ponto.model.Usuario;
import br.com.geovan.Ponto.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	Logger _log = LoggerFactory.getLogger(UsuarioService.class);
	
	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	public Optional<Usuario> create(String Email) {
		try {
			Optional<Usuario> byEmail = findByEmail(Email);
			if (byEmail.isPresent())
				return byEmail;
			else {
				Usuario usuario = new Usuario();
				usuario.setEmail(Email);
				usuario.setNome(Email);
				usuario.setSenha(Email);
				usuario = usuarioRepository.save(usuario);
				if (usuario != null) {
					return Optional.of(usuario);
				} 
				throw new RuntimeException("Problemas na criacao do usuario.");
			}
		} catch (Exception e) {
			_log.info("Problemas: {}", e.getMessage(), e);
			return Optional.empty();
		}
	}

}
