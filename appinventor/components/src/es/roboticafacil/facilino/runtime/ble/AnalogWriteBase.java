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
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.Ev3BinaryParser;
import com.google.appinventor.components.runtime.util.Ev3Constants;
import com.google.appinventor.components.runtime.util.SdkLevel;

import com.google.appinventor.components.runtime.util.YailList;
import es.roboticafacil.facilino.runtime.ble.Facilino;
import es.roboticafacil.facilino.runtime.ble.FacilinoBase;

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
/**
 * An analog output (analog write) component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
//@SimpleObject (external =true)
@DesignerComponent(version = Facilino.VERSION,
                   description = "An PWM ditial output (analog write) component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/pwm_signal_16x16.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class AnalogWriteBase  extends FacilinoActuatorBase {
	protected byte _pin;
	protected int _value;
	
	/**
	 * Creates a new Facilino component.
	 */
	public AnalogWriteBase(ComponentContainer container) {
		super(container,"AnalogWrite",FacilinoBase.TYPE_ANALOG_WRITE);
	_pin=3;
	}
	
	@SimpleProperty(description = "The PWM digital output pin.",category = PropertyCategory.BEHAVIOR)
	public byte Pin() {
		return _pin;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,defaultValue = "3")
	@SimpleProperty(description = "The PWM digital output pin.")
	public void Pin(byte pin) {
		_pin = pin;
	}
	
	@SimpleProperty(description = "The PWM digital output value.",category = PropertyCategory.BEHAVIOR)
	public int Value() {
		return _value;
	}
	
	public abstract void Set(int value);
}