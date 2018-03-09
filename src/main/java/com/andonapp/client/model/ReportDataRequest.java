package com.andonapp.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Request object for reporting process data.
 */
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDataRequest {

	/**
	 * Name of the organization. Will be automatically supplied in the client.
	 */
	private String orgName;
	
	/**
	 * Name of the line. Cannot be null.
	 */
	private String lineName;
	
	/**
	 * Name of the station. Cannot be null.
	 */
	private String stationName;
	
	/**
	 * Must be 'PASS' or 'FAIL'.
	 */
	private String passResult;
	
	/**
	 * Time in seconds spent processing. Cannot be null.
	 */
	private Long processTimeSeconds;
	
	/**
	 * Reason of failure. Null on success.
	 */
	private String failReason;
	
	/**
	 * Freeform notes on failure. Maybe be null, depending on org settings.
	 */
	private String failNotes;
	
}
