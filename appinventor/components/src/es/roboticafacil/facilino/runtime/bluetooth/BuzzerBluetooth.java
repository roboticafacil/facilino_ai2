// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.bluetooth;

import java.util.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.UsesLibraries;
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
import es.roboticafacil.facilino.runtime.bluetooth.Facilino;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBase;
import es.roboticafacil.facilino.runtime.bluetooth.BuzzerBase;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBluetoothClient;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBluetoothActuator;

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
/**
 * A buzzer component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A buzzer component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/buzzer_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
//@UsesLibraries(libraries = "es.roboticafacil.facilino.runtime.bluetooth.jar")
public class BuzzerBluetooth  extends BuzzerBase implements FacilinoBluetoothActuator {
	
	/**
	 * Creates a new Facilino component.
	 */
	public BuzzerBluetooth(ComponentContainer container) {
		super(container);
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoBluetoothClient(FacilinoBluetoothClient facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}
	
	@Override
	@SimpleFunction(description = "Sends a tone telegram to Facilino.")
	public void Tone(int frequency, int duration) {
		if (_facilino instanceof FacilinoBluetoothClient)
			((FacilinoBluetoothClient)_facilino).SendBytes(toneTelegram(frequency,duration));
	}
	
	@Override
	@SimpleFunction(description = "Sends a predefine melody telegram to Facilino.")
	public void Song(int number) {
		if (_facilino instanceof FacilinoBluetoothClient)
			((FacilinoBluetoothClient)_facilino).SendBytes(predefMelodyTelegram(number));
	}
	
	private YailList toneTelegram(int frequency, int duration)
	{
		byte[] bytes = new byte[9];
		bytes[0]='@';
		bytes[1]=FacilinoBluetoothClient.CMD_BUZZER_TONE;
		bytes[2]=5;
		bytes[3]=(byte)_pin;
		bytes[4]=(byte)((frequency>>8)&0xFF);
		bytes[5]=(byte)(frequency&0xFF);
		bytes[6]=(byte)((duration>>8)&0xFF);
		bytes[7]=(byte)(duration&0xFF);
		bytes[8]='*';
		int n=bytes.length;
		Object[] array = new Object[n];
		for (int i=0;i<n;i++)
		array[i]=(Object)bytes[i];
		YailList list = YailList.makeList(array);
		return list;
	}
	
	private YailList predefMelodyTelegram(int number)
	{
		byte[] bytes = new byte[6];
		bytes[0]='@';
		bytes[1]=FacilinoBluetoothClient.CMD_BUZZER_MELODY;
		bytes[2]=2;
		bytes[3]=(byte)_pin;
		bytes[4]=(byte)number;
		bytes[5]='*';
		int n=bytes.length;
		Object[] array = new Object[n];
		for (int i=0;i<n;i++)
		array[i]=(Object)bytes[i];
		YailList list = YailList.makeList(array);
		return list;
	}

}
