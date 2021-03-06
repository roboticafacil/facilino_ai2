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
import es.roboticafacil.facilino.common.Facilino;
import es.roboticafacil.facilino.common.FacilinoBase;

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
/**
 * A buzzer component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
//@SimpleObject (external =true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class BuzzerBase  extends FacilinoActuatorBase {
	protected byte _pin;
	
	/**
	 * Creates a new Facilino component.
	 */
	public BuzzerBase(ComponentContainer container) {
		super(container,"Buzzer",FacilinoBase.TYPE_BUZZER);
	_pin=3;
	}
	
	@SimpleProperty(description = "The buzzer pin.",
									category = PropertyCategory.BEHAVIOR)
	public byte Pin() {
		return _pin;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "3")
	@SimpleProperty(description = "The buzzer pin.")
	public void Pin(byte pin) {
		_pin = pin;
	}
	
	@SimpleFunction(description = "Sends a tone telegram to Facilino.")
	public void Tone(int frequency, int duration) {};
	
	/*@SimpleFunction(description = "Sends a number with the predefined melody telegram to Facilino.")
	public void PredefMelody(byte number) {
		_facilino.SendBytes(predefMelodyTelegram(number));
	}*/
	
	@SimpleFunction(description = "Sends a melody telegram to Facilino.")
	public void Melody(YailList melody) {};

}
