package com.andonapp.client;

import java.io.IOException;

import com.andonapp.client.exception.AndonAppException;
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

public class AndonAppClient {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER = "Bearer ";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	
	private static final String DEFAULT_ENDPOINT = "https://portal.andonapp.com/public/api/v1";
	private static final String REPORT_DATA_PATH = "data/report";
	private static final String UPDATE_STATUS_PATH = "station/update";
	
	private OkHttpClient httpClient;
	private ObjectMapper objectMapper;
	
	private HttpUrl endpointUrl;
	private String authHeaderValue;
	
	public AndonAppClient (String apiToken) {
		this(apiToken, new OkHttpClient());
	}
	
	public AndonAppClient (String apiToken, OkHttpClient httpClient) {
		Precondition.checkNotBlank(apiToken, "apiToken cannot be blank");
		this.authHeaderValue = BEARER + apiToken;
		this.httpClient = Precondition.checkNotNull(httpClient, "httpClient cannot be null");
		this.objectMapper = new ObjectMapper();
		this.endpointUrl = HttpUrl.parse(DEFAULT_ENDPOINT);
	}
	
	public void setEndpoint(String endpoint) {
		Precondition.checkNotBlank(endpoint, "endpoint cannot be blank");
		this.endpointUrl = HttpUrl.parse(endpoint);
	}
	
	public void reportData(ReportDataRequest request) throws IOException {
		executeRequest(request, REPORT_DATA_PATH);
	}
	
	public void updateStationStatus(UpdateStationStatusRequest request) throws IOException {
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
