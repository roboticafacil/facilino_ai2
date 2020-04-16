// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.ble;

import java.util.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.UsesLibraries;
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
import es.roboticafacil.facilino.runtime.ble.BooleanVariableBase;
import es.roboticafacil.facilino.runtime.ble.FacilinoBLESensorActuator;
import es.roboticafacil.facilino.runtime.ble.FacilinoBLEClient;

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
public class BooleanVariableBLE  extends BooleanVariableBase implements FacilinoBLESensorActuator {
	/**
	 * Creates a new Facilino component.
	 */
	public BooleanVariableBLE(ComponentContainer container) {
		super(container);
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoBLEClient(FacilinoBLEClient ble) {
		this.BLEDevice(ble);
	}

	@Override
	@SimpleFunction(description = "Sends a digital read request to Facilino and waits for response.")
	public void Update() throws InterruptedException{
		_dataDispatched=false;
		int maxWait=200;
		if (_ble instanceof FacilinoBLEClient)
		{
			((FacilinoBLEClient)_ble).SendBytes(readTelegram());
			while((!_dataDispatched)&&(maxWait>0)){ Thread.sleep(1); maxWait--;};
			if (maxWait<=0)
				this.Timeout(Facilino.ERROR_DATA_NOT_DISPATCHED);
		}
	}

	@Override
	@SimpleFunction(description = "Sends a digital read request to Facilino.")
	public void Request() {
		_dataDispatched=false;
		if (_ble instanceof  FacilinoBLEClient)
			((FacilinoBLEClient)_ble).SendBytes(readTelegram());
	}

	@Override
	@SimpleFunction(description = "Sets a boolean variable.")
	public void Set(boolean value) {
		_value=value;
		_prev_value=_value;
		if (_ble instanceof FacilinoBLEClient)
			((FacilinoBLEClient)_ble).SendBytes(setTelegram(value));
	}

	@Override
	@SimpleFunction(description = "Toggles a boolean variable by sending a telegram to Facilino.")
	public void Toggle() {
		_value=!_value;
		_prev_value=_value;
		if (_ble instanceof FacilinoBLEClient)
			((FacilinoBLEClient)_ble).SendBytes(setTelegram(_value));
	}

	private YailList readTelegram() {
		byte[] bytes = new byte[5];
		bytes[0]='@';
		bytes[1]=FacilinoBase.CMD_BOOLEAN_VAR_READ_REQ;
		bytes[2]=1;
		bytes[3]=(byte)_index;
		bytes[4]='*';
		int n=bytes.length;
		Object[] array1 = new Object[n];
		for (int i=0;i<n;i++)
		array1[i]=(Object)bytes[i];
		YailList list = YailList.makeList(array1);
		return list;
	}

	public void dispatchData(byte cmd, byte[] data) {
		if (cmd==FacilinoBase.CMD_BOOLEAN_VAR_READ_RESP){
			if (data[0]==_index)
			{
				_value = (data[1]==1) ? true : false;
				if (_firstTime)
					_firstTime=false;
				else if (_value!=_prev_value)
					Changed(_value);
				_prev_value=_value;
				Received(_value);
				_dataDispatched=true;
			}
		}
	}

	private YailList setTelegram(boolean value)
	{
		byte[] bytes = new byte[6];
		bytes[0]='@';
		bytes[1]=FacilinoBase.CMD_BOOLEAN_VAR_WRITE_REQ;
		bytes[2]=2;
		bytes[3]=(byte)_index;
		bytes[4]=(value) ? (byte)1 : (byte)0;
		bytes[5]='*';
		int n=bytes.length;
		Object[] array = new Object[n];
		for (int i=0;i<n;i++)
		array[i]=(Object)bytes[i];
		YailList list = YailList.makeList(array);
		return list;
	}
}
