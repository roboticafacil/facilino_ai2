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
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.Ev3BinaryParser;
import com.google.appinventor.components.runtime.util.Ev3Constants;
import com.google.appinventor.components.runtime.util.SdkLevel;

import com.google.appinventor.components.runtime.util.YailList;
import es.roboticafacil.facilino.common.Facilino;
import es.roboticafacil.facilino.common.FacilinoBase;
import es.roboticafacil.facilino.common.BooleanVariableBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWebSensorActuator;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;

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
public class BooleanVariableWeb extends BooleanVariableBase implements FacilinoWebSensorActuator {
	/**
	 * Creates a new Facilino component.
	 */
	public BooleanVariableWeb(ComponentContainer container) {
		super(container);
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoWeb(FacilinoWeb facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}

	@SimpleFunction(description = "Sends a digital read request to Facilino and waits for response.")
	public void Update() throws InterruptedException{
		_dataDispatched=false;
		if (_facilino instanceof FacilinoWeb)
		{
			((FacilinoWeb)_facilino).GetURL(buildURL());
			while(!_dataDispatched){ Thread.sleep(1);};
		}
	}

	@SimpleFunction(description = "Sends a digital read request to Facilino.")
	public void Request() {
		_dataDispatched=false;
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURL());
	}

	@SimpleFunction(description = "Sets a boolean variable.")
	public void Set(boolean value) {
		_value=value;
		_prev_value=_value;
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURL(value));
	}

	@SimpleFunction(description = "Toggles a boolean variable by sending a telegram to Facilino.")
	public void Toggle() {
		_value=!_value;
		_prev_value=_value;
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURL(_value));
	}
	
	private String buildURL()
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_index;
		return str;
	}
	
	private String buildURL(boolean value)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_index;
		str+="?value=";
		str+=value? "true" : "false";
		return str;
	}

	public void dispatchContents(JSONObject json)
	{
		_dataDispatched=true;
	}
}
