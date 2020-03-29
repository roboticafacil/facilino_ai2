// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.web;

import java.util.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
//import com.google.appinventor.components.common.YaVersion;
//import com.google.appinventor.components.runtime.util.SdkLevel;
//import com.google.appinventor.components.runtime.util.TimerInternal;
import com.google.appinventor.components.runtime.util.YailList;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import es.roboticafacil.facilino.runtime.web.Facilino;
import es.roboticafacil.facilino.runtime.web.FacilinoBase;
import es.roboticafacil.facilino.runtime.web.FacilinoSensor;
import es.roboticafacil.facilino.runtime.web.IPsNamesResponse;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import java.net.InetAddress;
import java.util.Formatter;
import java.math.BigInteger;
import android.content.Context;

//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.util.Log;

//import java.lang.Class;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.lang.reflect.*;
import java.util.Set;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONException;
/**
 * A Facilino web component that provides a low-level interface to Facilino
 * with functions to send HTTP commands to Facilino. Based on App Inventor 2 Web component
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
  
  
@DesignerComponent(version = Facilino.VERSION,
                   description = "A Facilino web component that provides a low-level interface to Facilino " +
                                 "with functions to send HTTP requests to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/facilino_logo_ai2_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE" +
								   "android.permission.ACCESS_WIFI_STATE," +
                                   "android.permission.ACCESS_NETWORK_STATE")
public class FacilinoWeb  extends FacilinoBase implements IPsNamesResponse {
	
	public static final String ERROR_JSON="Error in JSON object";
	
	private static class InvalidRequestHeadersException extends Exception {
		/*
		 * errorNumber could be:
		 * ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_LIST
		 * ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_TWO_ELEMENTS
		 */
		final int errorNumber;
		final int index;				 // the index of the invalid header

		InvalidRequestHeadersException(int errorNumber, int index) {
			super();
			this.errorNumber = errorNumber;
			this.index = index;
		}
	}

	/**
	 * BuildRequestDataException can be thrown from buildRequestData.
	 * It is thrown if the list passed to buildRequestData contains an item that is not a list.
	 * It is thrown if the list passed to buildRequestData contains an item that is a list whose size is
	 * not 2.
	 */
	// VisibleForTesting
	static class BuildRequestDataException extends Exception {
		/*
		 * errorNumber could be:
		 * ErrorMessages.ERROR_WEB_BUILD_REQUEST_DATA_NOT_LIST
		 * ErrorMessages.ERROR_WEB_BUILD_REQUEST_DATA_NOT_TWO_ELEMENTS
		 */
		final int errorNumber;
		final int index;				 // the index of the invalid header

		BuildRequestDataException(int errorNumber, int index) {
			super();
			this.errorNumber = errorNumber;
			this.index = index;
		}
	}
	
	/**
	 * The CapturedProperties class captures the current property values from a Web component before
	 * an asynchronous request is made. This avoids concurrency problems if the user changes a
	 * property value after initiating an asynchronous request.
	 */
	private static class CapturedProperties {
	final String urlString;
	//final URL url;
	URL url;
	final Map<String, String> requestHeaders;

	CapturedProperties(FacilinoWeb web) throws MalformedURLException, InvalidRequestHeadersException {
		urlString = "http://"+web.host+":"+web.port+web.urlString;
		url = new URL("http",web.host,web.port,web.urlString);
		//requestHeaders = processRequestHeaders(web.requestHeaders);
		requestHeaders = web.requestHeaders;
	}
	}
	
	private final Activity activity;
	
	protected String urlString;
	protected URL url;
	protected String host;
	protected int port=80;
	protected int scanTimeOut=1000;
	protected Map<String, String> requestHeaders = new HashMap<>();
	private FacilinoWebManager webManager;
	private List<String> hosts = new ArrayList<String>();
	private int from=1;
	private int to=255;
	
	private static final String LOG_TAG = "FacilinoWeb";
	
	/**
	 * Creates a new Facilino component.
	 */
	public FacilinoWeb(ComponentContainer container) {
		super(container.$form(),"FacilinoWeb",FacilinoBase.TYPE_MANAGER_HTTP);
		activity = container.$context();
		requestHeaders.put("Host","192.168.1.100:80");
		requestHeaders.put("Connection","keep-alive");
	}
	
	@SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description ="Host IP")
	public String Host() {
		return host;
	}
	
	/*@DesignerProperty(
			editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
			defaultValue = "192.168.1.100")*/
	@SimpleProperty
	public void Host(String h) {
		int firstSpace = h.indexOf(" ");
		if (firstSpace != -1) {
		  h = h.substring(0, firstSpace);
		}
		host=h;
		requestHeaders.put("Host",host+":"+port);
	}
	
	@SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description ="Scan TimeOut")
	public int ScanTimeOut() {
		return scanTimeOut;
	}
	
	@DesignerProperty(
			editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
			defaultValue = "1000")
	@SimpleProperty
	public void ScanTimeOut(int time) {
		scanTimeOut=time;
	}
	
	@SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description ="Initial Scan Address")
	public int From() {
		return from;
	}
	
	@DesignerProperty(
			editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
			defaultValue = "1")
	@SimpleProperty
	public void From(int f) {
		from=f;
	}
	
	@SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description ="Final Scan Address")
	public int To() {
		return to;
	}
	
	@DesignerProperty(
			editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
			defaultValue = "255")
	@SimpleProperty
	public void To(int t) {
		to=t;
	}
	
	/*@SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description ="Port number")
	public int Port() {
		return port;
	}
	
	@DesignerProperty(
			editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
			defaultValue = "80")
	@SimpleProperty
	public void Port(int p) {
		port=p;
		requestHeaders.put("Host",host+":"+port);
	}*/
	
	@SimpleFunction(description = "Gets the list of available hosts (list with IPs addresses and names)")
	public void Scan()
	{
		hosts.clear();
		webManager = new FacilinoWebManager(activity);
		webManager.delegate = this;
		webManager.execute();
	}
	
	@SimpleFunction(description = "Cancel current scan")
	public void CancelScanning()
	{
		webManager.cancel(true);
	}
	
	@SimpleEvent(description = "Gets the list of hosts")
	@Override
	public void ScanComplete(List<String> hosts){
     EventDispatcher.dispatchEvent(this, "ScanComplete",hosts);
   }
   
   @SimpleEvent(description = "Scanning has been cancelled")
	@Override
	public void ScanCancelled(){
     EventDispatcher.dispatchEvent(this, "ScanCancelled");
   }
   
   @SimpleEvent(description = "Scanning error")
	@Override
	public void ScanningError(String error){
     EventDispatcher.dispatchEvent(this, "ScanningError",error);
   }
   
   @SimpleEvent(description = "Scanning host")
	@Override
	public void ScanningHost(String host){
     EventDispatcher.dispatchEvent(this, "ScanningHost",host);
   }
   
   @SimpleEvent(description = "A new host has been detected")
	@Override
	public void HostDetected(String ip, String hostname){
		hosts.add(ip+" "+hostname);
		EventDispatcher.dispatchEvent(this, "HostDetected",ip,hostname);
   }
	
	@SimpleEvent(description = "Get URL string")
		public void GotResponse(String url, int responseCode, String responseType, String responseContent){
				EventDispatcher.dispatchEvent(this, "GotResponse",url);
		}
		
	@Override
	public List<String> GetHosts()
	{
		return hosts;
	}

	
	public void GetURL(String URL)
	{
		final String METHOD = "Get";
		urlString=URL;
		final CapturedProperties webProps = capturePropertyValues(METHOD);
		AsynchUtil.runAsynchronously(new Runnable() {
			@Override
			public void run() {
				try {
					performRequest(webProps, null, "GET");
				} catch (PermissionException e) {
					form.dispatchPermissionDeniedEvent(FacilinoWeb.this, METHOD, e);
				} catch (Exception e) {
					Log.e(LOG_TAG, "ERROR_UNABLE_TO_GET", e);
					form.dispatchErrorOccurredEvent(FacilinoWeb.this, METHOD,
							ErrorMessages.ERROR_WEB_UNABLE_TO_GET, webProps.urlString);
				}
			}
		});
		
	}
	
	@SimpleEvent(description = "JSON Error.")
	public void JSONError(String error) {
			EventDispatcher.dispatchEvent(this, "JSONError",error);
	}
	
	/*
	 * Perform a HTTP GET or POST request.
	 * This method is always run on a different thread than the event thread. It does not use any
	 * property value fields because the properties may be changed while it is running. Instead, it
	 * uses the parameters.
	 * If either postData or postFile is non-null, then a post request is performed.
	 * If both postData and postFile are non-null, postData takes precedence over postFile.
	 * If postData and postFile are both null, then a get request is performed.
	 * If saveResponse is true, the response will be saved in a file and the GotFile event will be
	 * triggered. responseFileName specifies the name of the	file.
	 * If saveResponse is false, the GotText event will be triggered.
	 *
	 * This method can throw an IOException. The caller is responsible for catching it and
	 * triggering the appropriate error event.
	 *
	 * @param webProps the captured property values needed for the request
	 * @param postData the data for the post request if it is not coming from a file, can be null
	 * @param postFile the path of the file containing data for the post request if it is coming from
	 *								 a file, can be null
	 *
	 * @throws IOException
	 */
	private void performRequest(final CapturedProperties webProps, byte[] postData, String httpVerb) 
	throws IOException {

		// Open the connection.
		HttpURLConnection connection = openConnection(webProps, httpVerb);
		if (connection != null) {
			try {
				if (postData != null) {
					writeRequestData(connection, postData);
				}

				// Get the response.
				final int responseCode = connection.getResponseCode();
				final String responseType = getResponseType(connection);
				
				final String responseContent = getResponseContent(connection);

					// Dispatch the event.
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GotResponse(webProps.urlString,responseCode,responseType,responseContent);
							//if (!responseContent.isEmpty())
							{
								try
								{
									JSONObject json= new JSONObject(responseContent);
									for (FacilinoSensor sensor: attachedSensors)
									{
										if (sensor instanceof FacilinoWebSensor)
											((FacilinoWebSensor)sensor).dispatchContents(json);
										if (sensor instanceof FacilinoWebSensorActuator)
											((FacilinoWebSensorActuator)sensor).dispatchContents(json);
									}
								}
								catch (JSONException e)
								{
									//FacilinoWeb.JSONError(FacilinoWeb.ERROR_JSON);
								}
							}
						}
					});

			} finally {
				connection.disconnect();
			}
		}
	}

	/**
	 * Open a connection to the resource and set the HTTP action to PUT or DELETE if it is one of
	 * them. GET would be the default, and POST is set in writeRequestData or writeRequestFile
	 * @param webProps the properties of the connection, set as properties in the component
	 * @param httpVerb One of GET/POST/PUT/DELETE
	 * @return a HttpURL Connection
	 * @throws IOException
	 * @throws ClassCastException
	 * @throws ProtocolException thrown if the method in setRequestMethod is not correct
	 */
	private static HttpURLConnection openConnection(CapturedProperties webProps, String httpVerb)
			throws IOException, ClassCastException, ProtocolException {

		HttpURLConnection connection = (HttpURLConnection) webProps.url.openConnection();
		if (connection!=null){
			if (httpVerb.equals("PUT") || httpVerb.equals("DELETE")){
				// Set the Request Method; GET is the default, and if it is a POST, it will be marked as such
				// with setDoOutput in writeRequestFile or writeRequestData
				connection.setRequestMethod(httpVerb);
			}

			// Request Headers
			for (Map.Entry<String, String> header : webProps.requestHeaders.entrySet()) {
				String name = header.getKey();
				String value = header.getValue();
				connection.addRequestProperty(name,value);
			}
		}

		return connection;
	}
	
	/*
	 * Captures the current property values that are needed for an HTTP request. If an error occurs
	 * while validating the Url or RequestHeaders property values, this method calls
	 * form.dispatchErrorOccurredEvent and returns null.
	 *
	 * @param functionName the name of the function, used when dispatching errors
	 */
	private CapturedProperties capturePropertyValues(String functionName) {
		try {
			return new CapturedProperties(this);
		} catch (MalformedURLException e) {
			form.dispatchErrorOccurredEvent(this, functionName,
					ErrorMessages.ERROR_WEB_MALFORMED_URL, urlString);
		} catch (InvalidRequestHeadersException e) {
			form.dispatchErrorOccurredEvent(this, functionName, e.errorNumber, e.index);
		}
		return null;
	}
	
	/*
	 * Converts request headers (a YailList) into the structure that can be used with the Java API
	 * (a Map<String, String>). If the request headers contains an invalid element, an
	 * InvalidRequestHeadersException will be thrown.
	 */
	private static Map<String, String> processRequestHeaders(YailList list)
			throws InvalidRequestHeadersException {
		Map<String, String> requestHeadersMap = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			Object item = list.getObject(i);
			// Each item must be a two-element sublist.
			if (item instanceof YailList) {
				YailList sublist = (YailList) item;
				if (sublist.size() == 2) {
					// The first element is the request header field name.
					String fieldName = sublist.getObject(0).toString();
					// The second element contains the request header field values.
					String fieldValue = sublist.getObject(1).toString();

					// Build an entry (key and values) for the requestHeadersMap.
					String key = fieldName;
					String value = fieldValue;
					// Put the entry into the requestHeadersMap.
					requestHeadersMap.put(key, value);
				} else {
					// The sublist doesn't contain two elements.
					throw new InvalidRequestHeadersException(
							ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_TWO_ELEMENTS, i + 1);
				}
			} else {
				// The item isn't a sublist.
				throw new InvalidRequestHeadersException(
						ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_LIST, i + 1);
			}
		}
		return requestHeadersMap;
	}
	
	private static String getResponseType(HttpURLConnection connection) {
		String responseType = connection.getContentType();
		return (responseType != null) ? responseType : "";
	}
	
	private static String getResponseContent(HttpURLConnection connection) throws IOException {
		// Use the content encoding to convert bytes to characters.
		String encoding = connection.getContentEncoding();
		if (encoding == null) {
			encoding = "UTF-8";
		}
		InputStreamReader reader = new InputStreamReader(getConnectionStream(connection), encoding);
		try {
			int contentLength = connection.getContentLength();
			StringBuilder sb = (contentLength != -1)
					? new StringBuilder(contentLength)
					: new StringBuilder();
			char[] buf = new char[1024];
			int read;
			while ((read = reader.read(buf)) != -1) {
				sb.append(buf, 0, read);
			}
			return sb.toString();
		} finally {
			reader.close();
		}
	}
	
	private static void writeRequestData(HttpURLConnection connection, byte[] postData)
			throws IOException {
		// According to the documentation at
		// http://developer.android.com/reference/java/net/HttpURLConnection.html
		// HttpURLConnection uses the GET method by default. It will use POST if setDoOutput(true) has
		// been called.
		connection.setDoOutput(true); // This makes it something other than a HTTP GET.
		// Write the data.
		connection.setFixedLengthStreamingMode(postData.length);
		BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
		try {
			out.write(postData, 0, postData.length);
			out.flush();
		} finally {
			out.close();
		}
	}
	
	private static InputStream getConnectionStream(HttpURLConnection connection) {
		// According to the Android reference documentation for HttpURLConnection: If the HTTP response
		// indicates that an error occurred, getInputStream() will throw an IOException. Use
		// getErrorStream() to read the error response.
		try {
			return connection.getInputStream();
		} catch (IOException e1) {
			// Use the error response.
			return connection.getErrorStream();
		}
	}
}
