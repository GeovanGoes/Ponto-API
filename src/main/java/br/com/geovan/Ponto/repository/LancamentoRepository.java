package br.com.geovan.Ponto.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geovan.Ponto.model.Lancamento;
import br.com.geovan.Ponto.model.Usuario;

/***
 * 
 * @author geovan.goes
 *
 */
public interface LancamentoRepository extends CrudRepository<Lancamento, Long>
{
	/***
	 * 
	 * @param dataLancamento
	 * @return
	 */
	Lancamento getLancamentoByDataHoraLancamentoAndUsuario(LocalDateTime dataHoraLancamento, Usuario usuario);
	
	/**
	 * 
	 * @param usuario 
	 * @return
	 */
	List<Lancamento> findAllByUsuarioOrderByDataHoraLancamento(Usuario usuario);
}
