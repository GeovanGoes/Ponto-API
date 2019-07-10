/**
 * 
 */
package br.com.geovan.Ponto.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/***
	 * 
	 * @param date
	 * @return
	 */
	public LocalDateTime dateToLocalDateTime (Date date)
	{
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
}
