/**
 * 
 */
package br.com.geovan.Ponto.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author geovan.goes
 *
 */
public class DateUtil 
{	
	/***
	 * 
	 * @param localDateTime
	 * @return
	 */
	public Date localDateTimeToDate(LocalDateTime localDateTime)
	{
		return localDateTime != null ? Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
	}
	
	/***
	 * 
	 * @param date
	 * @return
	 */
	public LocalDateTime dateToLocalDateTime (Date date)
	{
		return date != null ? LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
	}
	
	public static final String DEFAULT_PATTERN_FOR_DATE_TIME = "yyyy-MM-dd HH:mm";
	public static final String DEFAULT_PATTERN_FOR_TIME = "HH:mm";
	public static final String DEFAULT_PATTERN_FOR_DATE = "dd-MM-yyyy";
}
