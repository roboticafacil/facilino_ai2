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
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.YailList;

import com.google.appinventor.components.runtime.util.SdkLevel;
import es.roboticafacil.facilino.runtime.bluetooth.Facilino;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBase;
import es.roboticafacil.facilino.runtime.bluetooth.ServoContBase;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBluetoothClient;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBluetoothActuator;

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
                   description = "A continuous rotation servo component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/servo_cont_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
//@UsesLibraries(libraries = "es.roboticafacil.facilino.runtime.bluetooth.jar")
public class ServoContBluetooth  extends ServoContBase implements FacilinoBluetoothActuator {
  /**
   * Creates a new Facilino component.
   */
  public ServoContBluetooth(ComponentContainer container) {
    super(container);
  }
  
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoBluetoothClient(FacilinoBluetoothClient facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}
  
  @Override
  @SimpleFunction(description = "Sends a servo telegram to Facilino.")
  public void Move(int velocity) {
	  if (_facilino instanceof FacilinoBluetoothClient)
			((FacilinoBluetoothClient)_facilino).SendBytes(moveTelegram(velocity));
  }
  
  private YailList moveTelegram(int velocity)
  {
	  if (velocity>100)
		  velocity=100;
	  else if (velocity<-100)
		  velocity=-100;
	  velocity = (((velocity*90)/100+90));
	  byte _servoCmd = (byte)velocity;
	  byte[] bytes = new byte[6];
	  bytes[0]='@';
	  bytes[1]=FacilinoBluetoothClient.CMD_SERVO_CONT;
	  bytes[2]=2;
	  bytes[3]=(byte)_pin;
	  bytes[4]=_servoCmd;
	  bytes[5]='*';
	  int n=bytes.length;
	  Object[] array1 = new Object[n];
	  for (int i=0;i<n;i++)
		array1[i]=(Object)bytes[i];
	  YailList list = YailList.makeList(array1);
	  return list;
  }

}
