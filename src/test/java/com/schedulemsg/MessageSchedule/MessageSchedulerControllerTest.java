package com.schedulemsg.MessageSchedule;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.schedulemsg.constants.Constants;
import com.schedulemsg.controller.MessageSchedulerController;
import com.schedulemsg.payload.MessageScheduleRequest;
import com.schedulemsg.payload.MessageScheduleResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MessageSchedulerController.class })
@AutoConfigureMockMvc
public class MessageSchedulerControllerTest {

	@Autowired
	RestTemplate restTemplate;

	final String baseUrl = "http://localhost:8080/scheduleMessage";

	@Mock
	private ZonedDateTime dateTime;

	MessageScheduleRequest messageScheduleRequest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.mock(ZonedDateTime.class);
		LocalDateTime dateTime = LocalDateTime.parse("2016-03-04 11:30:40",
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		messageScheduleRequest = new MessageScheduleRequest();
		messageScheduleRequest.setMessage("Printing first message");
		messageScheduleRequest.setDateTime(dateTime);
		messageScheduleRequest.setTimeZone(ZoneId.of("America/New_York"));
	}

	@Test
	public void testscheduleMessage() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MessageScheduleRequest> request = new HttpEntity<>(messageScheduleRequest, headers);
		try {
			URI uri = new URI(baseUrl);
			ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
			Assert.assertEquals(202, response.getStatusCodeValue());
		} catch (HttpClientErrorException | URISyntaxException ex) {
			Assert.assertFalse(false);
		}

	}

	@Test
	public void testscheduleMessageTimeFail() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MessageScheduleRequest> request = new HttpEntity<>(messageScheduleRequest, headers);
		try {
			URI uri = new URI(baseUrl);
			ResponseEntity<MessageScheduleResponse> response = restTemplate.postForEntity(uri, request,
					MessageScheduleResponse.class);
			ZonedDateTime dateTime = ZonedDateTime.of(messageScheduleRequest.getDateTime(),
					messageScheduleRequest.getTimeZone());
			Mockito.when(dateTime.isBefore(ZonedDateTime.now())).thenReturn(true);
			Assert.assertEquals(Constants.TIME_SCHEDULE_FAIL_STATUS, response.getBody().getMessage());
		} catch (HttpClientErrorException | URISyntaxException ex) {
			Assert.assertFalse(false);
		}
	}

	@After
	public void tearDown() {
		messageScheduleRequest = null;
	}

}
