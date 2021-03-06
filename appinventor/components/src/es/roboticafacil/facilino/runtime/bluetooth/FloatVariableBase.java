// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.bluetooth;

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
import es.roboticafacil.facilino.runtime.bluetooth.Facilino;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBase;

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
                   description = "A float variable component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/float.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class FloatVariableBase  extends FacilinoActuatorSensorBase {
	protected byte _index;
	protected float _value;
	protected boolean _firstTime;

	/**
	 * Creates a new Facilino component.
	 */
	public FloatVariableBase(ComponentContainer container) {
		super(container,"FloatVariable",FacilinoBase.TYPE_FLOAT_VAR);
	_index=0;
	}

	@SimpleProperty(description = "Float index position.",
									category = PropertyCategory.BEHAVIOR)
	public byte Index() {
		return _index;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "0")
	@SimpleProperty(description = "Float index position.")
	public void Index(byte index) {
		_index = index;
	}

	@SimpleProperty(description = "The float value.",
									category = PropertyCategory.BEHAVIOR)
	public float Value() {
		return _value;
	}

	public abstract void Update() throws InterruptedException;

	public abstract void Request();

	public abstract void Set(float value);

	@SimpleEvent(description = "Float variable read event.")
	public void Received(float value){
			EventDispatcher.dispatchEvent(this, "Received",value);
	}
}
