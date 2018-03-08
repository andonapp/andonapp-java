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
 * Request object for update a station status.
 */
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStationStatusRequest {

	/**
	 * Name of the organization. Cannot be null.
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
	 * Must be 'GREEN', 'YELLOW', or 'RED'.
	 */
	private String statusColor;
	
	/**
	 * Reason for the change. May be null.
	 */
	private String statusReason;
	
	/**
	 * Notes on the change. May be null, depending on org settings.
	 */
	private String statusNotes;
	
}
