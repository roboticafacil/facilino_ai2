// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.common;

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
import es.roboticafacil.facilino.common.Facilino;
import es.roboticafacil.facilino.common.FacilinoBase;
//import com.google.appinventor.components.runtime.util.YailList;

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
import org.json.JSONObject;
/**
 * An analog read component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "An analog read component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/analog_signal_16x16.png")
//@SimpleObject (external =true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class AnalogReadBase  extends FacilinoSensorBase {
	protected byte _pin;
	protected int _value;
	
	/**
	 * Creates a new Facilino component.
	 */
	public AnalogReadBase(ComponentContainer container) {
		super(container,"AnalogRead",FacilinoBase.TYPE_ANALOG_READ);
		_dataDispatched=false;
		_pin=0;
	}
	
	protected AnalogReadBase(ComponentContainer container, String logTag, byte type)
	{
		super(container,logTag,type);
		_dataDispatched=false;
		_pin=0;
	}
	
	@SimpleProperty(description = "The analog read pin.",
									category = PropertyCategory.BEHAVIOR)
	public byte AnalogPin() {
		return _pin;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "0")
	@SimpleProperty(description = "The analog pin")
	public void AnalogPin(byte pin) {
		_pin = pin;
	}
	
	@SimpleProperty(description = "The analog input value (last received).",
									category = PropertyCategory.BEHAVIOR)
	public int Value() {
		return _value;
	}
	
	@SimpleFunction(description = "Sends an analog read request to Facilino and waits for response.")
	public void Update() throws InterruptedException {};
	
	@SimpleFunction(description = "Sends an analog read request to Facilino.")
	public void Request() {};
	
	@SimpleEvent(description = "Analog read event.")
	public void Received(int value){
			EventDispatcher.dispatchEvent(this, "Received",value);
	}
}
