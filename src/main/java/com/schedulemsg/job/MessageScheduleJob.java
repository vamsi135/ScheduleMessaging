package com.schedulemsg.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class MessageScheduleJob extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(MessageScheduleJob.class);

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

		JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
		String message = jobDataMap.getString("message");
		printScheduledMessage(message);
	}

	private void printScheduledMessage(String message) {
		try {
			logger.info("Scheduled Message = "+message);
		} catch (Exception ex) {
			logger.error("Failed to Schedule message", message);
		}
	}
}
