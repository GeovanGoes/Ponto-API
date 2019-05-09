package br.com.geovan.Ponto.to;

import java.io.Serializable;
import java.util.Date;

public class LancamentoTO implements Serializable
{
	
	private Date dataHora;

	public LancamentoTO(Date dataHora) 
	{
		super();
		this.dataHora = dataHora;
	}

	public Date getDataHora() 
	{
		return dataHora;
	}

	public void setDataHora(Date dataHora) 
	{
		this.dataHora = dataHora;
	}
}
