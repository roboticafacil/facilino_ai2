package es.roboticafacil.facilino.runtime.web;

import java.util.List;

public interface IPsNamesResponse {
    void ScanComplete(List<String> reachableDevices);
	void HostDetected(String ip, String hostname);
	void ScanningHost(String host);
	void ScanCancelled();
	void ScanningError(String error);
	int ScanTimeOut();
	List<String> GetHosts();
}