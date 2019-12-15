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
import es.roboticafacil.facilino.runtime.web.Facilino;
import es.roboticafacil.facilino.runtime.web.FacilinoBase;
import es.roboticafacil.facilino.runtime.web.BooleanVariableBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWebSensorActuator;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
import org.json.JSONObject;
import org.json.JSONException;
/**
 * A buzzer component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A integer variable component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/integer.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public class IntegerVariableWeb extends IntegerVariableBase implements FacilinoWebSensorActuator {
	/**
	 * Creates a new Facilino component.
	 */
	public IntegerVariableWeb(ComponentContainer container) {
		super(container);
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoWeb(FacilinoWeb facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}

	@Override
	@SimpleFunction(description = "Sends an integer read request to Facilino and waits for response.")
	public void Update(int index) throws InterruptedException{
		_dataDispatched=false;
		int maxWait=400;
		if (_facilino instanceof FacilinoWeb)
		{
			((FacilinoWeb)_facilino).GetURL(buildURL(index));
			while((!_dataDispatched)&&(maxWait>0)){ Thread.sleep(1); maxWait--;};
			if (maxWait<=0)
				this.Timeout(Facilino.ERROR_DATA_NOT_DISPATCHED);
		}
	}

	@Override
	@SimpleFunction(description = "Sends an integer read request to Facilino.")
	public void Request(int index) {
		_dataDispatched=false;
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURL(index));
	}

	@SimpleFunction(description = "Sets an integer variable.")
	public void Set(int index, int value) {
		_value=value;
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURL(index,_value));
	}
	
	private String buildURL(int index)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=index;
		return str;
	}
	
	private String buildURL(int index,int value)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=index;
		str+="?value=";
		str+=value;
		return str;
	}

	public void dispatchContents(JSONObject json)
	{
		int index=-1;
		int value=0;
		Iterator<String> it = json.keys();
		while(it.hasNext())
		{
			String key = it.next();
			if (key.equals(logTag))
			{
				try{
					JSONObject data = json.getJSONObject(key);
					Iterator<String> it1 = data.keys();
					while(it1.hasNext())
					{
						String key1 = it1.next();
						if (key1.equals("index"))
							index=data.getInt(key1);
						if (key1.equals("value"))
							value=data.getInt(key1);
					}
				}
				catch (JSONException e)
				{
					if (_facilino instanceof FacilinoWeb)
						((FacilinoWeb)_facilino).JSONError(FacilinoWeb.ERROR_JSON);
					return;
				}
			}
		}
		_value=value;
		Received(index,value);
		_dataDispatched=true;
	}
}
