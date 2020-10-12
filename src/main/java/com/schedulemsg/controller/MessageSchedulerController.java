package com.schedulemsg.controller;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.schedulemsg.constants.Constants;
import com.schedulemsg.job.MessageScheduleJob;
import com.schedulemsg.payload.MessageScheduleRequest;
import com.schedulemsg.payload.MessageScheduleResponse;

@RestController
public class MessageSchedulerController {
	private static final Logger logger = LoggerFactory.getLogger(MessageSchedulerController.class);

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private JobDataMap jobDataMap;

	private Trigger trigger;

	/**
	 * @param messageScheduleRequest
	 * @return messageScheduleResponse
	 */
	@PostMapping("/scheduleMessage")
	public ResponseEntity<MessageScheduleResponse> scheduleMessage(
			@Valid @RequestBody MessageScheduleRequest messageScheduleRequest) {
		try {
			ZonedDateTime dateTime = ZonedDateTime.of(messageScheduleRequest.getDateTime(),
					messageScheduleRequest.getTimeZone());
			if (dateTime.isBefore(ZonedDateTime.now())) {
				MessageScheduleResponse messageScheduleResponse = new MessageScheduleResponse(false,
						Constants.TIME_SCHEDULE_FAIL_STATUS);
				return ResponseEntity.badRequest().body(messageScheduleResponse);
			}

			JobDetail jobDetail = buildJobDetail(messageScheduleRequest);
			trigger = buildJobTrigger(jobDetail, dateTime);
			scheduler.scheduleJob(jobDetail, trigger);

			MessageScheduleResponse messageScheduleResponse = new MessageScheduleResponse(true,
					jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), Constants.SCHEDULED_SUCCESS_STATUS);
			return new ResponseEntity<MessageScheduleResponse>(messageScheduleResponse,
					HttpStatus.valueOf(Constants.SUCCESS_STATUS_CODE));
		} catch (SchedulerException ex) {
			logger.error("Error scheduling message sending", ex);

			MessageScheduleResponse messageScheduleResponse = new MessageScheduleResponse(false,
					"Error scheduling message. Please try later!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageScheduleResponse);
		}
	}

	/**
	 * @param messageScheduleRequest
	 * @return jobDetail object
	 */
	private JobDetail buildJobDetail(MessageScheduleRequest messageScheduleRequest) {
		jobDataMap.put("message", messageScheduleRequest.getMessage());

		return JobBuilder.newJob(MessageScheduleJob.class).withIdentity(UUID.randomUUID().toString(), "message-jobs")
				.withDescription("Send Message Job").usingJobData(jobDataMap).storeDurably().build();
	}

	/**
	 * @param jobDetail
	 * @param startAt
	 * @return jobTrigger
	 */
	private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
		return TriggerBuilder.newTrigger().forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "message-triggers")
				.withDescription("Schedule Message Trigger").startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
	}

	/**
	 * @return the scheduler
	 */
	/*
	 * public Scheduler getScheduler() { return scheduler; }
	 * 
	 *//**
		 * @param scheduler
		 *            the scheduler to set
		 *//*
		 * public void setScheduler(Scheduler scheduler) { this.scheduler =
		 * scheduler; }
		 */
}
