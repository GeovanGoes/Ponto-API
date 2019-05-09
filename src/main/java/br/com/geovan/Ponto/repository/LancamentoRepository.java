package br.com.geovan.Ponto.repository;

import java.util.Date;

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
	Lancamento getLancamentoByDataHoraLancamento(Date dataHoraLancamento);
}
