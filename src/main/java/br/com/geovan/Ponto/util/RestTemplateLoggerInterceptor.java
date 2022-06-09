package br.com.geovan.Ponto.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;



public class RestTemplateLoggerInterceptor implements ClientHttpRequestInterceptor {
	
	private static final Logger LOG = LoggerFactory.getLogger(RestTemplateLoggerInterceptor.class);
	
	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		traceRequest(httpRequest, body);
		ClientHttpResponse clientHttpResponse = clientHttpRequestExecution.execute(httpRequest, body);
		traceResponse(clientHttpResponse);
		return clientHttpResponse;
	}
	
	private void traceRequest(HttpRequest httpRequest, byte[] body) throws IOException {
		try {
			try {
				LOG.info(LoggerUtils.getLogRequest(LoggerUtils.getURL(httpRequest), httpRequest.getMethod().toString(), LoggerUtils.getHeaders(httpRequest), LoggerUtils.getParameters(httpRequest), new String(body, "UTF-8")));
			} catch (Exception e) {
				LOG.info(LoggerUtils.getLogRequest(LoggerUtils.getURL(httpRequest), httpRequest.getMethod().toString(), LoggerUtils.getHeaders(httpRequest), LoggerUtils.getParameters(httpRequest), new String(body, "UTF-8")));
			}
		} catch (Exception e) {
			LOG.error("Problemas ao gerar request log", e);
		}
		
	}
	
	private void traceResponse(ClientHttpResponse clientHttpResponse) throws IOException {
		try {
			try {
				LOG.info(LoggerUtils.getLogResponse(clientHttpResponse.getStatusCode().value(), LoggerUtils.getHeaders(clientHttpResponse), IOUtils.toString(clientHttpResponse.getBody(), "UTF-8")));
			} catch (Exception e) {
				LOG.info(LoggerUtils.getLogResponse(clientHttpResponse.getStatusCode().value(), LoggerUtils.getHeaders(clientHttpResponse), IOUtils.toString(clientHttpResponse.getBody(), "UTF-8")));
			}
		} catch (Exception e) {
			LOG.error("Problemas ao gerar response log", e);
		}
	}
	
	public static RestTemplate getRestTemplateLogged() {
		RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new RestTemplateLoggerInterceptor());
		restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}
	
}
