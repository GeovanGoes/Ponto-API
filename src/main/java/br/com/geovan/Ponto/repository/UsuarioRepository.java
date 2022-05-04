package br.com.geovan.Ponto.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.geovan.Ponto.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>
{
	Optional<Usuario> findByEmail(String email);
}
