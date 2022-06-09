package br.com.geovan.Ponto.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import br.com.geovan.Ponto.model.Feriado;
import br.com.geovan.Ponto.repository.FeriadoRepository;

@Service
public class FeriadoService {

	@Autowired
	private FeriadoRepository repository;
	
	Logger _log = LoggerFactory.getLogger(FeriadoService.class);
	
	
	public Optional<Feriado> obterFeriado(LocalDate data) {
		return repository.findByData(data);
	}
	
	public void carregarBaseDeDados(String anoReferencia) throws RestClientException, URISyntaxException {
		//RestTemplate restTemplateLogged = RestTemplateLoggerInterceptor.getRestTemplateLogged();//
		RestTemplate restTemplateLogged = new RestTemplate();
		
		ResponseEntity<String> forEntity = restTemplateLogged.exchange(new URI("https://brasilapi.com.br/api/banks/v1"), HttpMethod.GET, null, String.class);
		if (forEntity.getBody() != null) {
			System.out.println(forEntity.getBody());
		}
	}	
	
	public static void main(String[] args) throws RestClientException, URISyntaxException, IOException {
		
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
		  .url("https://brasilapi.com.br/api/feriados/v1/2022")
		  .method("GET", null)
		  .build();
		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		List<Feriado> readValue = mapper.readValue(response.body().string(), new TypeReference<List<Feriado>>() {});
		for (Feriado feriado : readValue) {
			System.out.println(feriado.getNome());
		}
		//new FeriadoService().carregarBaseDeDados(null);
				
	}
}
