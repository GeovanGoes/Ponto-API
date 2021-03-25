/**
 * 
 */
package br.com.geovan.Ponto.to;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author geovan.goes
 *
 */
public class Dia 
{
	private String data;
	private List<String> registros;
	
	/**
	 * @param data
	 * @param registros
	 */
	public Dia(String data, List<String> registros) {
		super();
		this.data = data;
		this.registros = registros;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the registros
	 */
	public List<String> getRegistros() {
		return registros;
	}
	/**
	 * @param registros the registros to set
	 */
	public void setRegistros(List<String> registros) {
		this.registros = registros;
	}
}
