package br.com.geovan.Ponto;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@SpringBootApplication
public class PontoApplication 
{
	
	public static void main(String[] args) 
	{
		System.out.println(TimeZone.getDefault());
		SpringApplication.run(PontoApplication.class, args);
	}
}
