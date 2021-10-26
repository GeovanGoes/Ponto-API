package br.com.geovan.Ponto.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.com.geovan.Ponto.client.SelfClient;

//@Configuration
//@EnableScheduling
public class KeepYourselfAlive {
	
	private static final Logger LOG = LoggerFactory.getLogger(KeepYourselfAlive.class);
	
	@Autowired
	private SelfClient client;
	
	//@Scheduled(cron = "${application.scheduled.keepyourselfalive.cron:0 */1 * * * ?}")
	public void process () {
		System.out.println("Comecando");
		client.callMe();
		System.out.println("Acabando");
	}
}
