package br.com.geovan.Ponto.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class Lancamento implements Serializable
{
	@Id
	@GeneratedValue
	private Long id;
	
	private LocalDateTime dataHoraLancamento;
	
	
	public Lancamento() 
	{
	}
	
	public Lancamento(LocalDateTime dataHoraLancamento) 
	{
		
		super();
		this.dataHoraLancamento = dataHoraLancamento;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public LocalDateTime getDataHoraLancamento()
	{
		return dataHoraLancamento;
	}

	public void setDataHoraLancamento(LocalDateTime dataHoraLancamento)
	{
		this.dataHoraLancamento = dataHoraLancamento;
	}
	
	
}
