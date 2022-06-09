package br.com.geovan.Ponto.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.geovan.Ponto.model.Feriado;

public interface FeriadoRepository extends CrudRepository<Feriado, Long> {
	
	Optional<Feriado> findByData(LocalDate data);

}
