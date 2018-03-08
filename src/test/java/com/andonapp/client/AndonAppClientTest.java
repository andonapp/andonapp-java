package com.andonapp.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.andonapp.client.exception.AndonAppException;
import com.andonapp.client.exception.AndonInvalidRequestException;
import com.andonapp.client.exception.AndonResourceNotFoundException;
import com.andonapp.client.exception.AndonUnauthorizedRequestException;
import com.andonapp.client.model.ErrorResponse;
import com.andonapp.client.model.ReportDataRequest;
import com.andonapp.client.model.SpringErrorResponse;
import com.andonapp.client.model.UpdateStationStatusRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class AndonAppClientTest {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String API_TOKEN = "api-token";

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private MockWebServer server;
	
	private AndonAppClient client;
	
	private ObjectMapper objectMapper;
	
	@Before
	public void before() throws IOException {
		server = new MockWebServer();
		server.start();
		
		client = new AndonAppClient(API_TOKEN);
		client.setEndpoint(server.url("/").toString());
		
		objectMapper = new ObjectMapper();
	}
	
	@After
	public void after() throws IOException {
		server.shutdown();
	}
	
	@Test
	public void shouldThrowExceptionWhenApiTokenBlank() {
		exception.expect(IllegalArgumentException.class);
		new AndonAppClient("");
	}
	
	@Test
	public void shouldThrowExceptionWhenHttpClientNull() {
		exception.expect(IllegalArgumentException.class);
		new AndonAppClient(API_TOKEN, null);
	}
	
	@Test
	public void shouldReportDataWhenValidPassRequest() throws Exception {
		expectSuccess();
		
		ReportDataRequest request = ReportDataRequest.builder()
			.orgName("Demo")
			.lineName("line 1")
			.stationName("station 1")
			.passResult("PASS")
			.processTimeSeconds(100L)
			.build();

		client.reportData(request);
		
		verifyRequest(request, server.takeRequest());
	}
	
	@Test
	public void shouldReportDataWhenValidFailRequest() throws Exception {
		expectSuccess();
		
		ReportDataRequest request = ReportDataRequest.builder()
				.orgName("Demo")
				.lineName("line 1")
				.stationName("station 1")
				.passResult("FAIL")
				.processTimeSeconds(200L)
				.failReason("Test Failure")
				.failNotes("notes")
				.build();
		
		client.reportData(request);
		
		verifyRequest(request, server.takeRequest());
	}
	
	@Test
	public void shouldThrowExceptionWhenReportDataMissingOrgName() throws Exception {
		expectFailure(400, new ErrorResponse("INVALID_REQUEST", "orgName may not be empty"));
		
		ReportDataRequest request = ReportDataRequest.builder()
				.lineName("line 1")
				.stationName("station 1")
				.passResult("PASS")
				.processTimeSeconds(100L)
				.build();
		
		try {
			client.reportData(request);
			fail("no exception was thrown");
		} catch (AndonInvalidRequestException e) {
			assertEquals("orgName may not be empty", e.getMessage());
			verifyRequest(request, server.takeRequest());
		}
	}
	
	@Test
	public void shouldThrowExceptionWhenReportDataStationNotFound() throws Exception {
		expectFailure(400, new ErrorResponse("RESOURCE_NOT_FOUND", "Station not found."));
		
		ReportDataRequest request = ReportDataRequest.builder()
				.orgName("Demo")
				.lineName("line 1")
				.stationName("station 2")
				.passResult("PASS")
				.processTimeSeconds(100L)
				.build();
		
		try {
			client.reportData(request);
			fail("no exception was thrown");
		} catch (AndonResourceNotFoundException e) {
			assertEquals("Station not found.", e.getMessage());
			verifyRequest(request, server.takeRequest());
		}
	}
	
	@Test
	public void shouldThrowExceptionWhenReportDataInvalidPassResult() throws Exception {
		expectFailure(400, new ErrorResponse("INVALID_REQUEST", "'PAS' is not a valid pass result."));
		
		ReportDataRequest request = ReportDataRequest.builder()
				.orgName("Demo")
				.lineName("line 1")
				.stationName("station 1")
				.passResult("PAS")
				.processTimeSeconds(100L)
				.build();
		
		try {
			client.reportData(request);
			fail("no exception was thrown");
		} catch (AndonInvalidRequestException e) {
			assertEquals("'PAS' is not a valid pass result.", e.getMessage());
			verifyRequest(request, server.takeRequest());
		}
	}
	
	@Test
	public void shouldThrowExceptionWhenReportDataUnauthorized() throws Exception {
		expectFailure(401,
				new SpringErrorResponse("2018-03-07T16:15:19.033+0000", 401,
						"Unauthorized", "Unauthorized", "/public/api/v1/data/report"));
		
		ReportDataRequest request = ReportDataRequest.builder()
				.orgName("Demo")
				.lineName("line 1")
				.stationName("station 1")
				.passResult("PAS")
				.processTimeSeconds(100L)
				.build();
		
		try {
			client.reportData(request);
			fail("no exception was thrown");
		} catch (AndonUnauthorizedRequestException e) {
			assertEquals("Unauthorized", e.getMessage());
			verifyRequest(request, server.takeRequest());
		}
	}
	
	@Test
	public void shouldThrowExceptionWhenUnknownFailResponse() throws Exception {
		expectFailure(400, "{}");
		
		ReportDataRequest request = ReportDataRequest.builder()
				.orgName("Demo")
				.lineName("line 1")
				.stationName("station 1")
				.passResult("PAS")
				.processTimeSeconds(100L)
				.build();
		
		try {
			client.reportData(request);
			fail("no exception was thrown");
		} catch (AndonAppException e) {
			assertEquals("Status 400: {}", e.getMessage());
			verifyRequest(request, server.takeRequest());
		}
	}
	
	@Test
	public void shouldUpdateStatusToYellowWhenValidRequest() throws Exception {
		expectSuccess();
		
		UpdateStationStatusRequest request = UpdateStationStatusRequest.builder()
				.orgName("Demo")
				.lineName("line 1")
				.stationName("station 1")
				.statusColor("YELLOW")
				.statusReason("Missing parts")
				.statusNotes("notes")
				.build();
		
		client.updateStationStatus(request);
		
		verifyRequest(request, server.takeRequest());
	}
	
	@Test
	public void shouldUpdateStatusToGreenWhenValidRequest() throws Exception {
		expectSuccess();
		
		UpdateStationStatusRequest request = UpdateStationStatusRequest.builder()
				.orgName("Demo")
				.lineName("line 1")
				.stationName("station 1")
				.statusColor("GREEN")
				.build();
		
		client.updateStationStatus(request);
		
		verifyRequest(request, server.takeRequest());
	}
	
	private void verifyRequest(Object originalRequest, RecordedRequest recordedRequest) throws JsonProcessingException {
		assertEquals(objectMapper.writeValueAsString(originalRequest),
				recordedRequest.getBody().readUtf8());
		assertEquals("Bearer " + API_TOKEN,
				recordedRequest.getHeader(AUTHORIZATION_HEADER));
	}
	
	private void expectSuccess() {
		server.enqueue(new MockResponse()
				.setResponseCode(200)
				.addHeader("Content-Type", "application/json")
				.setBody(""));
	}
	
	private void expectFailure(int statusCode, Object errorResponse) throws JsonProcessingException {
		expectFailure(statusCode, objectMapper.writeValueAsString(errorResponse));
	}
	
	private void expectFailure(int statusCode, String response) {
		server.enqueue(new MockResponse()
				.setResponseCode(statusCode)
				.addHeader("Content-Type", "application/json")
				.setBody(response));
	}
	
}
