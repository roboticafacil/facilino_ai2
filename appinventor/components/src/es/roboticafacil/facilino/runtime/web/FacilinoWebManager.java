// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.web;

import java.util.*;
import java.lang.*;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import android.net.ConnectivityManager;
import java.util.Map;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.annotations.UsesPermissions;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import java.net.InetAddress;
import java.util.Formatter;
import java.math.BigInteger;
import android.content.Context;
import android.app.Activity;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;
import java.net.Socket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * A Facilino web component that provides a low-level interface to Facilino
 * with functions to send HTTP commands to Facilino. Based on App Inventor 2 Web component
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
  
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.ACCESS_WIFI_STATE," +
                                   "android.permission.ACCESS_NETWORK_STATE")
public class FacilinoWebManager  extends AsyncTask<Void, Void, List<String> > {
	
	private WeakReference<Activity> mContextRef;
	public IPsNamesResponse delegate = null;
	
	  public FacilinoWebManager(Activity context) {
		mContextRef = new WeakReference<Activity>(context);
	  }
	  
	  	/*private static boolean IsAddressReachable(String address, int port, int timeout) {
		try {
 
			try (Socket socket = new Socket()) {
				// Connects this socket to the server with a specified timeout value.
				socket.connect(new InetSocketAddress(address, port), timeout);
			}
			// Return true if connection successful
			return true;
		} catch (IOException e) {
			return false;
		}
	}*/

	  @Override
	  protected List<String> doInBackground(Void... voids) {
		  
		  List<String> reachableDevices = new ArrayList<String>();

		try {
		  Activity context = mContextRef.get();

		  if (context == null
			|| context.isFinishing()
			|| context.isDestroyed()) {
			// activity is no longer valid, don't do anything!
			return reachableDevices;
		}
			
			ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			WifiManager wm = (WifiManager)context.getSystemService(android.content.Context.WIFI_SERVICE);

			WifiInfo connectionInfo = wm.getConnectionInfo();
			int ipAddress = connectionInfo.getIpAddress();
			final String ipString = String.format("%d.%d.%d.%d",
				(ipAddress & 0xff),
				(ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));

			String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
			

			for (int i = 0; i < 256; i++) {
				try{
					  String testIp = prefix + i;
						delegate.ScanningHost(testIp);
					  if (isCancelled())  
							break;
						
					  InetAddress address = InetAddress.getByName(testIp);
					  boolean reachable = address.isReachable(delegate.ScanTimeOut());
					  if (reachable)
					  {
						  String hostName = address.getCanonicalHostName();
						  reachableDevices.add(testIp+" "+hostName);
						  delegate.HostDetected(testIp,hostName);
					  }
				}
				catch (UnknownHostException e)
				{
					delegate.ScanningError(e.toString());
				}
				catch (IOException e)
				{
					delegate.ScanningError(e.toString());
				}
			}
			
		} catch (Throwable t) {
			delegate.ScanningError(t.toString());
		}

	  return reachableDevices;
	}
	
	protected void onCancelled(){
            delegate.ScanCancelled();
			delegate.ScanComplete(delegate.GetHosts());
        }
	
	@Override
    public void onPostExecute(List<String> reachableDevices) {
	  delegate.ScanComplete(reachableDevices);
    }
}
