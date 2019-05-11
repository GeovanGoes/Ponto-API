package br.com.geovan.Ponto;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PontoApplication 
{
	
	void started()
	{
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	public static void main(String[] args) 
	{
		System.out.println(TimeZone.getDefault());
		SpringApplication.run(PontoApplication.class, args);
	}
}
