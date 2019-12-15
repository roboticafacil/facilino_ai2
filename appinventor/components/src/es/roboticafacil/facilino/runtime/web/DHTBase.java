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
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.SdkLevel;
import es.roboticafacil.facilino.runtime.web.FacilinoBase;
import es.roboticafacil.facilino.runtime.web.Facilino;
import com.google.appinventor.components.runtime.util.YailList;
//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;

/**
 * A sonar component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A DHT component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/dht11_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class DHTBase extends FacilinoSensorBase {
	protected byte _pin;
	protected int _temperature;
	protected int _humidity;
	protected int _lowTemperatureThreshold;
	protected int _lowHumidityThreshold;
	protected int _highTemperatureThreshold;
	protected int _highHumidityThreshold;
	
	/**
	 * Creates a new Facilino component.
	 */
	public DHTBase(ComponentContainer container) {
		super(container,"DHT",FacilinoBase.TYPE_SONAR);
		_dataDispatched=false;
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public byte Pin() {
		return _pin;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "0")
	@SimpleProperty(description = "The DHT pin")
	public void Pin(byte pin) {
		_pin = pin;
	}
	
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public int LowTemperatureThreshold() {
		return _lowTemperatureThreshold;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "15")
	@SimpleProperty(description = "Temperature threshold value to compare, if temperature is smaller than threshold then throws an event")
	public void LowTemperatureThreshold(int threshold) {
		_lowTemperatureThreshold = threshold;
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public int HighTemperatureThreshold() {
		return _highTemperatureThreshold;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "30")
	@SimpleProperty(description = "Temperature threshold value to compare, if temperature is bigger than threshold then throws an event")
	public void HighTemperatureThreshold(int threshold) {
		_highTemperatureThreshold = threshold;
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public int LowHumidityThreshold() {
		return _lowHumidityThreshold;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "20")
	@SimpleProperty(description = "Humidity threshold value to compare, if humidity is smaller than threshold then throws an event")
	public void LowHumidityThreshold(int threshold) {
		_lowHumidityThreshold = threshold;
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public int HighHumidityThreshold() {
		return _highHumidityThreshold;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "60")
	@SimpleProperty(description = "Humidity threshold value to compare, if humidity is bigger than threshold then throws an event")
	public void HighHumidityThreshold(int threshold) {
		_highHumidityThreshold = threshold;
	}
	
	@SimpleProperty(description = "The measured temperature (last received).",
									category = PropertyCategory.BEHAVIOR)
	public int Temperature() {
		return _temperature;
	}
	
	@SimpleProperty(description = "The measured humidity (last received).",
									category = PropertyCategory.BEHAVIOR)
	public int Humidity() {
		return _humidity;
	}
	
	public abstract void Update() throws InterruptedException;
	
	public abstract void Request();
		
	@SimpleEvent(description = "DHT temperature and humidity read event.")
	public void Received(int temperature, int humidity){
			EventDispatcher.dispatchEvent(this, "Received",temperature,humidity);
	}
	
	@SimpleEvent(description = "DHT low temperature event when the temperature is lower than the threshold.")
	public void LowTemperature(){
			EventDispatcher.dispatchEvent(this, "LowTemperature");
	}
	
	@SimpleEvent(description = "DHT high temperature event when the temperature is lower than the threshold.")
		public void HighTemperature(){
				EventDispatcher.dispatchEvent(this, "HighTemperature");
		}
	
	@SimpleEvent(description = "DHT low humidity event when the temperature is lower than the threshold.")
		public void LowHumidity(){
				EventDispatcher.dispatchEvent(this, "LowHumidity");
		}
	
	@SimpleEvent(description = "DHT low humidity event when the temperature is lower than the threshold.")
		public void HighHumidity(){
				EventDispatcher.dispatchEvent(this, "HighHumidity");
		}
}
