// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.ble;

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
import es.roboticafacil.facilino.runtime.ble.FacilinoBase;
import es.roboticafacil.facilino.runtime.ble.Facilino;
import com.google.appinventor.components.runtime.util.YailList;
//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
import org.json.JSONObject;
/**
 * A digital read component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A digital read component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/digital_signal_in_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class DigitalReadBase  extends FacilinoSensorBase {
	protected byte _pin;
	protected boolean _value;
	protected boolean _prev_value;
	protected boolean _firstTime;
	
	/**
	 * Creates a new Facilino component.
	 */
	public DigitalReadBase(ComponentContainer container) {
		super(container,"DigitalRead",FacilinoBase.TYPE_DIGITAL_READ);
		_dataDispatched=false;
		_firstTime=true;
		_pin=2;
	}
	
	@SimpleProperty(description = "The digital read pin.",
									category = PropertyCategory.BEHAVIOR)
	public byte Pin() {
		return _pin;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "2")
	@SimpleProperty(description = "The digital pin")
	public void Pin(byte pin) {
		_pin = pin;
	}
	
	@SimpleProperty(description = "The digital input value (last received).",
									category = PropertyCategory.BEHAVIOR)
	public boolean Value() {
		return _value;
	}
	
	public abstract void Update() throws InterruptedException;
	
	public abstract void Request();
	
	@SimpleEvent(description = "Digital read change event.")
	public void Changed(boolean value){
			EventDispatcher.dispatchEvent(this, "Changed",value);
	}
	
	@SimpleEvent(description = "Digital read event.")
	public void Received(boolean value){
			EventDispatcher.dispatchEvent(this, "Received",value);
	}
}
