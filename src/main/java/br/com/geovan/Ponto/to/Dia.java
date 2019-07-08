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
	private LocalDate data;
	private List<LocalTime> registros;
	
	/**
	 * @param data
	 * @param registros
	 */
	public Dia(LocalDate data, List<LocalTime> registros) {
		super();
		this.data = data;
		this.registros = registros;
	}
	/**
	 * @return the data
	 */
	public LocalDate getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(LocalDate data) {
		this.data = data;
	}
	/**
	 * @return the registros
	 */
	public List<LocalTime> getRegistros() {
		return registros;
	}
	/**
	 * @param registros the registros to set
	 */
	public void setRegistros(List<LocalTime> registros) {
		this.registros = registros;
	}
}
