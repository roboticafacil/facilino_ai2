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
import org.json.JSONObject;
/**
 * A buzzer component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A boolean variable component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/binary.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class BooleanVariableBase  extends FacilinoActuatorSensorBase {
	protected byte _index;
	protected boolean _value;
	protected boolean _prev_value;
	protected boolean _firstTime;

	/**
	 * Creates a new Facilino component.
	 */
	public BooleanVariableBase(ComponentContainer container) {
		super(container,"BooleanVariable",FacilinoBase.TYPE_BOOLEAN_VAR);
	_index=0;
	}

	@SimpleProperty(description = "Boolean index position.",
									category = PropertyCategory.BEHAVIOR)
	public byte Index() {
		return _index;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "0")
	@SimpleProperty(description = "Boolean index position.")
	public void Index(byte index) {
		_index = index;
	}

	@SimpleProperty(description = "The boolean value.",
									category = PropertyCategory.BEHAVIOR)
	public boolean Value() {
		return _value;
	}

	public abstract void Update() throws InterruptedException;

	public abstract void Request();

	public abstract void Set(boolean value);

	public abstract void Toggle();

	@SimpleEvent(description = "Boolean variable read change event.")
	public void Changed(boolean value){
			EventDispatcher.dispatchEvent(this, "Changed",value);
	}

	@SimpleEvent(description = "Boolean variable read event.")
	public void Received(boolean value){
			EventDispatcher.dispatchEvent(this, "Received",value);
	}
}
