/**
 * 
 */
package br.com.geovan.Ponto.to;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.geovan.Ponto.model.Feriado;
import br.com.geovan.Ponto.util.DateUtil;

/**
 * @author geovan.goes
 *
 */
public class Dia implements Comparable<Dia>
{
	@JsonIgnore
	private static DateTimeFormatter datePattern = DateTimeFormatter.ofPattern(DateUtil.DEFAULT_PATTERN_FOR_DATE);
	@JsonIgnore
	private static DateTimeFormatter timePattern = DateTimeFormatter.ofPattern(DateUtil.DEFAULT_PATTERN_FOR_TIME);
	
	private String data;
	@JsonIgnore
	private LocalDate localDate;
	private List<String> registros;
	@JsonIgnore
	private List<LocalTime> lancamentos;
	@JsonIgnore
	private LocalTime sum;
	private String soma;
	private Integer numeroDaSemana;
	private boolean feriado;
	private FeriadoTO dadosFeriado;
	
	
	/**
	 * @param data
	 * @param registros
	 */
	public Dia(LocalDate localDate, List<LocalTime> lancamentos) {
		super();
		this.localDate = localDate;
		this.lancamentos = lancamentos;
		this.data = this.localDate.format(datePattern);
		this.registros = this.lancamentos.stream().map(l -> l.format(timePattern)).collect(Collectors.toList());
		WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
		numeroDaSemana = localDate.get(weekFields.weekOfWeekBasedYear());
		
		int index = 0;
		int size = this.lancamentos.size();
		
		long sum = 0;
		for (LocalTime start : this.lancamentos) {
			int mod = index % 2;
			if (mod == 0) {
				if((index + 1) < size) {
					LocalTime end = this.lancamentos.get(index + 1);
					long between = ChronoUnit.SECONDS.between(start,end);
					sum = sum + between;
				}				
			}
			index = index+1;
		}
		this.sum = LocalTime.ofSecondOfDay(sum);
		
		this.soma = this.sum.format(timePattern);
			
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
	
	public LocalDate getLocalDate() {
		return localDate;
	}
	
	public String getSoma() {
		return soma;
	}
	
	public Integer getNumeroDaSemana() {
		return numeroDaSemana;
	}
	
	public void setFeriado(Feriado feriado) {
		this.feriado = true;
		this.dadosFeriado = new FeriadoTO(feriado.getTipo(), feriado.getNome());
	}
	
	@Override
	public int compareTo(Dia o) {
		if (o == null)
			return 1;
		else
			return o.getLocalDate().compareTo(this.getLocalDate());
	}
	
	
}
