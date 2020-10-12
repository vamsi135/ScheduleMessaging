package com.schedulemsg.app;

import org.quartz.JobDataMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.schedulemsg")
public class MessageScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageScheduleApplication.class, args);
	}

	@Bean
	public JobDataMap jobDataMap() {
		JobDataMap jobDataMap = new JobDataMap();
		return jobDataMap;
	}
}
