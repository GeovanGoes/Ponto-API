package br.com.geovan.Ponto.util;

import javax.servlet.DispatcherType;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
class RestControllerLoggerConfiguration {
	
	private static String[] patterns = { 
			"*"
			
	};
	
	@Bean
	public FilterRegistrationBean<HttpLoggingFilter> commonsRequestLoggingFilter() {
		HttpLoggingFilter httpLoggingFilter = new HttpLoggingFilter();
		FilterRegistrationBean<HttpLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<HttpLoggingFilter>();
		filterRegistrationBean.setName("restControllerLoggerFilter");
		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
		filterRegistrationBean.setFilter(httpLoggingFilter);
		filterRegistrationBean.addUrlPatterns(patterns);
		filterRegistrationBean.setOrder(Integer.MAX_VALUE);
		filterRegistrationBean.setAsyncSupported(Boolean.TRUE);
		return filterRegistrationBean;
	}
	
}
