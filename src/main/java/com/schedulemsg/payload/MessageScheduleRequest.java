package com.schedulemsg.payload;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MessageScheduleRequest {

	@NotEmpty
	private String message;

	@NotNull
	private LocalDateTime dateTime;

	@NotNull
	private ZoneId timeZone;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public ZoneId getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(ZoneId timeZone) {
		this.timeZone = timeZone;
	}
}
