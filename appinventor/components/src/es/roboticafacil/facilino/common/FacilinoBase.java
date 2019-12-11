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
//import com.google.appinventor.components.annotations.SimpleEvent;
//import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
//import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
//import com.google.appinventor.components.common.YaVersion;
//import com.google.appinventor.components.runtime.util.SdkLevel;
//import com.google.appinventor.components.runtime.util.TimerInternal;
//import com.google.appinventor.components.runtime.util.YailList;
import es.roboticafacil.facilino.common.Facilino;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
/**
 * A continuous servo component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "An abstract class that provides a low-level interface to Facilino ",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/Facilino_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class FacilinoBase  extends AndroidNonvisibleComponent implements Component {
	protected final String logTag;
	protected byte _type;
 
	
	protected List<FacilinoSensor> attachedSensors = new ArrayList<FacilinoSensor>();
	private List<FacilinoActuator> attachedActuators = new ArrayList<FacilinoActuator>();

	public static byte TYPE_DIGITAL_READ = 0x00;
	public static byte TYPE_DIGITAL_WRITE = 0x01;
	public static byte TYPE_ANALOG_READ = 0x02;
	public static byte TYPE_ANALOG_WRITE = 0x03;
	public static byte TYPE_PUSH_BUTTON = 0x04;
	public static byte TYPE_LED = 0x05;
	public static byte TYPE_SERVO = 0x06;
	public static byte TYPE_SERVO_CONT = 0x07;
	public static byte TYPE_SONAR = 0x08;
	public static byte TYPE_BUZZER = 0x09;
	public static byte TYPE_INFRARED = 0x10;
	public static byte TYPE_LED_MATRIX = 0x11;
	public static byte TYPE_LED_STRIP = 0x12;
	public static byte TYPE_GASMQX = 0x13;
	public static byte TYPE_BOOLEAN_VAR = 0x14;
	
	public static byte TYPE_MANAGER_BLUETOOTH = 0x00;
	public static byte TYPE_MANAGER_HTTP = 0x01;
	
	protected final Lock _mutex = new ReentrantLock(true);
	
	protected FacilinoBase(ComponentContainer container, String logTag, byte type) {
		this(container.$form(), logTag, type);
	}
	
	private FacilinoBase(Form form, String logTag, byte type) {
		super(form);
		this.logTag = logTag;
	_type=type;
	}
	
	
	@SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description ="Number of attached sensors")
	public int attachedSensors() {
		return attachedSensors.size();
	}

	public boolean attachSensor(FacilinoSensor sensor) {
		attachedSensors.add(sensor);
		return true;
	}

	public void detachSensor(FacilinoSensor sensor) {
		attachedSensors.remove(sensor);
	}
	
	public boolean attachActuator(FacilinoActuator actuator) {
		attachedActuators.add(actuator);
		return true;
	}

	public void detachActuator(FacilinoActuator actuator) {
		attachedActuators.remove(actuator);
	}
	
	public byte getType()
	{
		return _type;
	}
	
	public void lock()
	{
		_mutex.lock();
	}
	
	public void unlock()
	{
		_mutex.unlock();
	}
}
