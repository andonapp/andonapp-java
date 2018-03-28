# AndonApp Java Client

Java client library for reporting data to [Andon](https://www.andonapp.com/)

## Download

Get the client via Maven:

```xml
<dependency>
  <groupId>com.andonapp</groupId>
  <artifactId>andonapp</artifactId>
  <version>1.0.1</version>
</dependency>
```

or Gradle:

```gradle
compile 'com.andonapp:andonapp:1.0.1'
```

## Usage

In order to programmatically connect to Andon's APIs you must first generate an API token. This is done by logging into your Andon account, navigating to the [API settings page](https://portal.andonapp.com/settings/tokens), and generating a new token.  Make sure to record the token, and keep it secret.

Reference Andon's [getting started guide](https://drive.google.com/file/d/0B5cQI3VvgCT8UllmaENIazlwbGc/view) and [API guide](https://drive.google.com/file/d/0B5cQI3VvgCT8enNIZGN2QVo0STg/view) for complete details on these prerequisites

### Setting up the Client

Now that you have a token, create a client as follows:

```java
AndonAppClient andonClient = new DefaultAndonAppClient(orgName, apiToken);
```

If you need custom HTTP client settings you can use the alternate constructor, and supply a pre-configured HTTP client.

### Reporting Data

Here's an example of using the client to report a success:

```java
andonClient.reportData(ReportDataRequest.builder()
		.lineName("line 1")
		.stationName("station 1")
		.passResult("PASS")
		.processTimeSeconds(100L)
		.build());
```

And a failure:

```java
andonClient.reportData(ReportDataRequest.builder()
		.lineName("line 1")
		.stationName("station 1")
		.passResult("FAIL")
		.failReason("Test Failure")
		.failNotes("notes")
		.processTimeSeconds(100L)
		.build());
```

### Updating a Station Status

Here's an example of flipping a station to Red:

```java
andonClient.updateStationStatus(UpdateStationStatusRequest.builder()
		.lineName("line 1")
		.stationName("station 1")
		.statusColor("RED")
		.statusReason("Missing parts")
		.statusNotes("notes")
		.build());
```

And back to Green:

```java
andonClient.updateStationStatus(UpdateStationStatusRequest.builder()
		.lineName("line 1")
		.stationName("station 1")
		.statusColor("GREEN")
		.statusNotes("notes")
		.build());
```

## License

[Licensed under the MIT license](LICENSE).
