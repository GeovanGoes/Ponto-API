package br.com.geovan.Ponto.util;

import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

public abstract class LoggerUtils {
	
	private static final String NEW_LINE = "\n";
	private static final String DECORATOR = "==================================================";
	private static final String SPACE = " ";
	private static final String BLANK = "";
	private static final String COMMA = ",";
	private static final String EQUALS = "=";
	private static final String LEFT_SQUARE_BRACKET = "[";
	private static final String RIGHT_SQUARE_BRACKET = "]";
	private static final String REQUEST = "REQUEST";
	private static final String REQUEST_PRE_POS_FIX = DECORATOR + (SPACE + REQUEST + SPACE).replaceAll(".", "=") + DECORATOR;
	private static final String RESPONSE = "RESPONSE";
	private static final String RESPONSE_PRE_POS_FIX = DECORATOR + (SPACE + RESPONSE + SPACE).replaceAll(".", "=") + DECORATOR;
	private static final String URL_PREFIX = "URL: ";
	private static final String METHOD_PREFIX = "METHOD: ";
	private static final String HEADERS_PREFIX = "HEADERS: ";
	private static final String PARAMETERS_PREFIX = "PARAMETERS: ";
	private static final String BODY_PREFIX = "BODY: ";
	private static final String STATUS_CODE_PREFIX = "STATUS_CODE: ";
	private static final String STATUS_NAME_PREFIX = "STATUS NAME: ";
	private static final String PARAMETERS_IDENTIFIER = "?";
	private static final String PARAMETERS_SEPARATOR = "&";
	
	public LoggerUtils() {
		System.out.println();
	}
	
	public static String getLogRequest(String url, String method, String headers, String parameters, String body) {
		try {
			StringBuilder stringBuilder = new StringBuilder(NEW_LINE + REQUEST_PRE_POS_FIX + NEW_LINE + DECORATOR + SPACE + REQUEST + SPACE + DECORATOR);
			if (StringUtils.isNotBlank(url)) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(URL_PREFIX);
				stringBuilder.append(url);
			}
			if (StringUtils.isNotBlank(method)) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(METHOD_PREFIX);
				stringBuilder.append(method);
			}
			if (StringUtils.isNotBlank(headers)) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(HEADERS_PREFIX);
				stringBuilder.append(headers);
			}
			if (StringUtils.isNotBlank(parameters)) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(PARAMETERS_PREFIX);
				stringBuilder.append(parameters);
			}
			if (StringUtils.isNotBlank(body)) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(BODY_PREFIX + LEFT_SQUARE_BRACKET + NEW_LINE);
				stringBuilder.append(body);
				stringBuilder.append(NEW_LINE + RIGHT_SQUARE_BRACKET);
			}
			stringBuilder.append(NEW_LINE + DECORATOR + SPACE + REQUEST + SPACE + DECORATOR + NEW_LINE + REQUEST_PRE_POS_FIX + NEW_LINE);
			return stringBuilder.toString();
		} catch (Exception e) {
			return "Erro gerar log";
		}
	}
	
	public static String getLogResponse(Integer statusCode, String headers, String body) {
		try {
			StringBuilder stringBuilder = new StringBuilder(NEW_LINE + RESPONSE_PRE_POS_FIX + NEW_LINE + DECORATOR + SPACE + RESPONSE + SPACE + DECORATOR);
			if (statusCode != null) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(STATUS_CODE_PREFIX);
				stringBuilder.append(statusCode);
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(STATUS_NAME_PREFIX);
				stringBuilder.append(HttpStatus.resolve(statusCode).name());
			}
			if (StringUtils.isNotBlank(headers)) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(HEADERS_PREFIX);
				stringBuilder.append(headers);
			}
			if (StringUtils.isNotBlank(body)) {
				stringBuilder.append(NEW_LINE);
				stringBuilder.append(BODY_PREFIX + LEFT_SQUARE_BRACKET + NEW_LINE);
				stringBuilder.append(body);
				stringBuilder.append(NEW_LINE + RIGHT_SQUARE_BRACKET);
			}
			stringBuilder.append(NEW_LINE + DECORATOR + SPACE + RESPONSE + SPACE + DECORATOR + NEW_LINE + RESPONSE_PRE_POS_FIX + NEW_LINE);
			return stringBuilder.toString();
		} catch (Exception e) {
			return "Erro gerar log";
		}
	}
	
	public static String getHeaders(HttpServletRequest httpServletRequest) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				stringBuilder.append(stringBuilder.length() != 0 ? COMMA : BLANK);
				stringBuilder.append(headerName);
				stringBuilder.append(EQUALS);
				stringBuilder.append(httpServletRequest.getHeader(headerName));
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			return "Erro gerar headers log";
		}
	}
	
	public static String getHeaders(HttpServletResponse httpServletResponse) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			Iterator<String> headerNames = httpServletResponse.getHeaderNames().iterator();
			while (headerNames.hasNext()) {
				String headerName = headerNames.next();
				stringBuilder.append(stringBuilder.length() != 0 ? COMMA : BLANK);
				stringBuilder.append(headerName);
				stringBuilder.append(EQUALS);
				stringBuilder.append(httpServletResponse.getHeader(headerName));
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			return "Erro gerar headers log";
		}
	}
	
	public static String getHeaders(HttpRequest httpRequest) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			for (String headerName : httpRequest.getHeaders().keySet()) {
				for (String value : httpRequest.getHeaders().get(headerName)) {
					stringBuilder.append(stringBuilder.length() != 0 ? COMMA : BLANK);
					stringBuilder.append(headerName);
					stringBuilder.append(EQUALS);
					stringBuilder.append(value);
				}
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			return "Erro gerar headers log";
		}
	}
	
	public static String getHeaders(ClientHttpResponse clientHttpResponse) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			for (String headerName : clientHttpResponse.getHeaders().keySet()) {
				for (String value : clientHttpResponse.getHeaders().get(headerName)) {
					stringBuilder.append(stringBuilder.length() != 0 ? COMMA : BLANK);
					stringBuilder.append(headerName);
					stringBuilder.append(EQUALS);
					stringBuilder.append(value);
				}
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			return "Erro gerar headers log";
		}
	}
	
	public static String getURL(HttpRequest httpRequest) {
		try {
			return httpRequest.getURI().toString().replace(PARAMETERS_IDENTIFIER + httpRequest.getURI().getQuery(), BLANK);
		} catch (Exception e) {
			return "Erro gerar url log";
		}
	}
	
	public static String getParameters(HttpServletRequest httpServletRequest) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String parameterName = parameterNames.nextElement();
				stringBuilder.append(stringBuilder.length() != 0 ? COMMA : BLANK);
				stringBuilder.append(parameterName);
				stringBuilder.append(EQUALS);
				stringBuilder.append(httpServletRequest.getParameter(parameterName));
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			return "Erro gerar parameters log";
		}
	}
	
	public static String getParameters(HttpRequest httpRequest) {
		try {
			if (httpRequest.getURI() != null && StringUtils.isNotBlank(httpRequest.getURI().getQuery())) {
				return httpRequest.getURI().getQuery().replaceAll(PARAMETERS_SEPARATOR, ",");
			}
			return null;
		} catch (Exception e) {
			return "Erro gerar parameters log";
		}
	}
	
}
