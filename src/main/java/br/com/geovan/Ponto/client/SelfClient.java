package br.com.geovan.Ponto.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class SelfClient {
	
	
	public void callMe() {
		RestTemplate restTemplate = new RestTemplate();
		String string = restTemplate.getForObject("https://ponto-service.herokuapp.com/api/lancamentos", String.class);
		System.out.println(string  != null ? string : "null");
	}

}
