package com.andonapp.client;

import java.io.IOException;

import com.andonapp.client.model.ReportDataRequest;
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
	private static final String REPORT_DATA = "data/report";
	private static final String UPDATE_STATUS = "station/update";
	
	private OkHttpClient httpClient;
	private ObjectMapper objectMapper;
	
	private HttpUrl endpointUrl;
	private String authHeaderValue;
	
	public AndonAppClient (String apiToken) {
		this(apiToken, new OkHttpClient());
	}
	
	public AndonAppClient (String apiToken, OkHttpClient httpClient) {
		// TODO verify params
		this.authHeaderValue = BEARER + apiToken;
		this.httpClient = httpClient;
		this.objectMapper = new ObjectMapper();
		this.endpointUrl = HttpUrl.parse(DEFAULT_ENDPOINT);
	}
	
	public void setEndpoint(String endpoint) {
		this.endpointUrl = HttpUrl.parse(endpoint);
	}
	
	// TODO wrap exceptions
	public void reportData(ReportDataRequest request) throws IOException {
		String requestString = objectMapper.writeValueAsString(request);
		RequestBody body = RequestBody.create(JSON, requestString);
		
		Request httpRequest = new Request.Builder()
			.url(createUrl(REPORT_DATA))
			.post(body)
			.addHeader(AUTHORIZATION_HEADER, authHeaderValue)
			.build();
		
		try (Response response = httpClient.newCall(httpRequest).execute()) {
			if (!response.isSuccessful()) {
				// TODO model exceptions
				throw new RuntimeException(
						String.format("ReportDataRequest failed. %s: %s", response.code(), response.body().string()));
			}
		}
	}
	
	private HttpUrl createUrl(String path) {
		return endpointUrl.newBuilder().addPathSegments(path).build();
	}
	
}
