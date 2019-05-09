package br.com.geovan.Ponto.model;

import java.io.Serializable;
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
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraLancamento;
	
	
	public Lancamento() 
	{
	}
	
	public Lancamento(Date dataHoraLancamento) 
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

	public Date getDataHoraLancamento()
	{
		return dataHoraLancamento;
	}

	public void setDataHoraLancamento(Date dataHoraLancamento)
	{
		this.dataHoraLancamento = dataHoraLancamento;
	}
	
	
}
