package com.andonapp.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.andonapp.client.exception.AndonInvalidRequestException;
import com.andonapp.client.model.ErrorResponse;
import com.andonapp.client.model.ReportDataRequest;
import com.andonapp.client.model.UpdateStationStatusRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class AndonAppClientTest {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String API_TOKEN = "api-token";

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
	public void shouldThrowExceptionWhenMissingOrgName() throws Exception {
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
		server.enqueue(new MockResponse()
				.setResponseCode(statusCode)
				.addHeader("Content-Type", "application/json")
				.setBody(objectMapper.writeValueAsString(errorResponse)));
	}
	
}
