package br.com.geovan.Ponto.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Lancamento implements Serializable
{
	@Id
	@GeneratedValue
	private Long id;
	
	private LocalDateTime dataHoraLancamento;
	
	@ManyToOne
	private Usuario usuario;
	
	
	public Lancamento() 
	{
	}
	
	public Lancamento(LocalDateTime dataHoraLancamento, Usuario usuario) 
	{
		
		super();
		this.dataHoraLancamento = dataHoraLancamento;
		this.usuario = usuario;
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
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
