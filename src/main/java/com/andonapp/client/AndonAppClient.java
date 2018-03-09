package com.andonapp.client;

import java.io.IOException;

import com.andonapp.client.exception.*;
import com.andonapp.client.model.ErrorResponse;
import com.andonapp.client.model.ReportDataRequest;
import com.andonapp.client.model.SpringErrorResponse;
import com.andonapp.client.model.UpdateStationStatusRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Client for making requests to Andon. In order to use the client you must generate
 * an API token on the org settings page within Andon.
 * 
 * <p>The following is an example usage: <pre> {@code
 *
 *   AndonAppClient andonClient = new AndonAppClient(orgName, apiToken);
 *   andonClient.reportData(ReportDataRequest.builder()
 *           .lineName("line 1")
 *           .stationName("station 1")
 *           .passResult("PASS")
 *           .processTimeSeconds(120L)
 *           .build());
 * }</pre>
 */
public class AndonAppClient {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER = "Bearer ";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	
	private static final String DEFAULT_ENDPOINT = "https://portal.andonapp.com/public/api/v1";
	private static final String REPORT_DATA_PATH = "data/report";
	private static final String UPDATE_STATUS_PATH = "station/update";
	
	private OkHttpClient httpClient;
	private ObjectMapper objectMapper;
	
	private String orgName;
	private HttpUrl endpointUrl;
	private String authHeaderValue;
	
	/**
	 * Constructs a new Andon client using a default HTTP client.
	 * 
	 * @param orgName name of the organization
	 * @param apiToken API token for the organization
	 */
	public AndonAppClient (String orgName, String apiToken) {
		this(orgName, apiToken, new OkHttpClient());
	}
	
	/**
	 * Constructs a new Andon client using a custom HTTP client.
	 * 
	 * @param orgName name of the organization
	 * @param apiToken API token for the organization
	 * @param httpClient client to use to connect to Andon
	 */
	public AndonAppClient (String orgName, String apiToken, OkHttpClient httpClient) {
		this.orgName = Precondition.checkNotBlank(orgName, "orgName cannot be blank");
		Precondition.checkNotBlank(apiToken, "apiToken cannot be blank");
		this.authHeaderValue = BEARER + apiToken;
		this.httpClient = Precondition.checkNotNull(httpClient, "httpClient cannot be null");
		this.objectMapper = new ObjectMapper();
		this.endpointUrl = HttpUrl.parse(DEFAULT_ENDPOINT);
	}
	
	/**
	 * Changes the endpoint that requests are made to.
	 * 
	 * @param endpoint Andon endpoint to connect to
	 */
	public void setEndpoint(String endpoint) {
		Precondition.checkNotBlank(endpoint, "endpoint cannot be blank");
		this.endpointUrl = HttpUrl.parse(endpoint);
	}
	
	/**
	 * Reports the outcome of a process at a station to Andon.
	 * 
	 * <p>The following is an example usage: <pre> {@code
	 *
	 *   andonClient.reportData(ReportDataRequest.builder()
	 *           .lineName("line 1")
	 *           .stationName("station 1")
	 *           .passResult("FAIL")
	 *           .failReason("Test Failure")
	 *           .failNotes("notes")
	 *           .processTimeSeconds(120L)
	 *           .build());
	 * }</pre>
	 * 
	 * @param request ReportDataRequest
	 * @throws IOException if there are problems connecting to Andon
	 * @throws AndonAppException if there is a general request failure
	 * @throws AndonBadRequestException if there is something wrong with the request
	 * @throws AndonInternalErrorException if there is a failure within Andon
	 * @throws AndonInvalidRequestException if there are invalid request arguments
	 * @throws AndonResourceNotFoundException if a referenced station can't be found
	 * @throws AndonUnauthorizedRequestException if authorization fails
	 */
	public void reportData(ReportDataRequest request) throws IOException {
		request.setOrgName(orgName);
		executeRequest(request, REPORT_DATA_PATH);
	}

	/**
	 * Changes the status of a station in Andon.
	 * 
	 * <p>The following is an example usage: <pre> {@code
	 *
	 *   andonClient.updateStationStatus(UpdateStationStatusRequest.builder()
	 *           .lineName("line 1")
	 *           .stationName("station 1")
	 *           .statusColor("YELLOW")
	 *           .statusReason("Missing parts")
	 *           .statusNotes("notes")
	 *           .build());
	 * }</pre>
	 * 
	 * @param request UpdateStationStatusRequest
	 * @throws IOException if there are problems connecting to Andon
	 * @throws AndonAppException if there is a general request failure
	 * @throws AndonBadRequestException if there is something wrong with the request
	 * @throws AndonInternalErrorException if there is a failure within Andon
	 * @throws AndonInvalidRequestException if there are invalid request arguments
	 * @throws AndonResourceNotFoundException if a referenced station can't be found
	 * @throws AndonUnauthorizedRequestException if authorization fails
	 */
	public void updateStationStatus(UpdateStationStatusRequest request) throws IOException {
		request.setOrgName(orgName);
		executeRequest(request, UPDATE_STATUS_PATH);
	}

	private void executeRequest(Object request, String path) throws IOException {
		Precondition.checkNotNull(request, "request cannot be null");
		String requestString = objectMapper.writeValueAsString(request);
		RequestBody body = RequestBody.create(JSON, requestString);
		
		Request httpRequest = new Request.Builder()
			.url(createUrl(path))
			.post(body)
			.addHeader(AUTHORIZATION_HEADER, authHeaderValue)
			.build();
		
		try (Response response = httpClient.newCall(httpRequest).execute()) {
			if (!response.isSuccessful()) {
				processErrorResponse(response);
			}
		}
	}
	
	private void processErrorResponse(Response response) throws IOException {
		String responseBody = response.body().string();
		
		try {
			ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
			if (errorResponse.getErrorType() != null) {
				Exceptions.throwFromErrorResponse(errorResponse);
			} else {
				SpringErrorResponse springResponse = objectMapper.readValue(responseBody, SpringErrorResponse.class);
				Exceptions.throwFromSpringErrorResponse(springResponse);
			}
		} catch (IOException e) {
			throw new AndonAppException(
					String.format("Status %s: %s", response.code(), responseBody), e);
		}
		
		throw new AndonAppException(
				String.format("Status %s: %s", response.code(), responseBody));
	}

	private HttpUrl createUrl(String path) {
		return endpointUrl.newBuilder().addPathSegments(path).build();
	}
	
}
