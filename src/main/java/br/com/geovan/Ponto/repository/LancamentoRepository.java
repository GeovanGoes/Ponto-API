package br.com.geovan.Ponto.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.geovan.Ponto.model.Lancamento;

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
	Lancamento getLancamentoByDataHoraLancamento(LocalDateTime dataHoraLancamento);
	
	/**
	 * 
	 * @return
	 */
	List<Lancamento> findAllByOrderByDataHoraLancamento();
}
