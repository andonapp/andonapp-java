package com.andonapp.client;

import java.io.IOException;

import com.andonapp.client.exception.AndonAppException;
import com.andonapp.client.exception.AndonBadRequestException;
import com.andonapp.client.exception.AndonInternalErrorException;
import com.andonapp.client.exception.AndonInvalidRequestException;
import com.andonapp.client.exception.AndonResourceNotFoundException;
import com.andonapp.client.exception.AndonUnauthorizedRequestException;
import com.andonapp.client.model.ReportDataRequest;
import com.andonapp.client.model.UpdateStationStatusRequest;

/**
 * Client for making requests to Andon. In order to use the client you must generate
 * an API token on the org settings page within Andon.
 * 
 * <p>The following is an example usage: <pre> {@code
 *
 *   AndonAppClient andonClient = new DefaultAndonAppClient(orgName, apiToken);
 *   andonClient.reportData(ReportDataRequest.builder()
 *           .lineName("line 1")
 *           .stationName("station 1")
 *           .passResult("PASS")
 *           .processTimeSeconds(120L)
 *           .build());
 * }</pre>
 */
public interface AndonAppClient {

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
	void reportData(ReportDataRequest request) throws IOException;

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
	void updateStationStatus(UpdateStationStatusRequest request) throws IOException;

}