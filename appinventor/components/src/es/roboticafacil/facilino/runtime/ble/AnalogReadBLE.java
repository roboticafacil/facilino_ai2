// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.ble;

import java.util.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.annotations.DesignerComponent;
//import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.PropertyTypeConstants;
//import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
//import com.google.appinventor.components.common.YaVersion;
//import com.google.appinventor.components.runtime.util.SdkLevel;
import es.roboticafacil.facilino.runtime.ble.Facilino;
import es.roboticafacil.facilino.runtime.ble.FacilinoBase;
import es.roboticafacil.facilino.runtime.ble.AnalogReadBase;
import es.roboticafacil.facilino.runtime.ble.FacilinoBLESensor;
import es.roboticafacil.facilino.runtime.ble.FacilinoBLEClient;
import com.google.appinventor.components.runtime.util.YailList;

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
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
//@UsesLibraries(libraries = "es.roboticafacil.facilino.runtime.bluetooth.jar")
public class AnalogReadBLE  extends AnalogReadBase implements FacilinoBLESensor {
	/**
	 * Creates a new Facilino component.
	 */
	public AnalogReadBLE(ComponentContainer container) {
		super(container);
	}
	
	protected AnalogReadBLE(ComponentContainer container, String logTag, byte type)
	{
		super(container,logTag,type);
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoBLEClient(FacilinoBLEClient ble) {
		this.BLEDevice(ble);
	}
	
	@Override
	@SimpleFunction(description = "Sends an analog read request to Facilino and waits for response.")
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
	@SimpleFunction(description = "Sends an analog read request to Facilino.")
	public void Request() {
		_dataDispatched=false;
		if (_ble instanceof FacilinoBLEClient)
			((FacilinoBLEClient)_ble).SendBytes(readTelegram());
	}
	
	private YailList readTelegram() {
		byte[] bytes = new byte[5];
		bytes[0]='@';
		bytes[1]=FacilinoBase.CMD_ANALOG_READ_REQ;
		bytes[2]=1;
		bytes[3]=(byte)(_pin);
		bytes[4]='*';
		int n=bytes.length;
		Object[] array1 = new Object[n];
		for (int i=0;i<n;i++)
		array1[i]=(Object)bytes[i];
		YailList list = YailList.makeList(array1);
		return list;
	}

	@Override
	public void dispatchData(byte cmd,byte[] data) {
		if (cmd==FacilinoBase.CMD_ANALOG_READ_RESP){
			if (data[0]==_pin)
			{
				_value = (((int)data[1]<<8)&0xFF00)|(((int)data[2])&0x00FF);
				Received(_value);
				_dataDispatched=true;
			}
		}
	}
}
