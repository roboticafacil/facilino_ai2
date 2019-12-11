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
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.YailList;

import es.roboticafacil.facilino.common.Facilino;
import es.roboticafacil.facilino.common.FacilinoBase;
import es.roboticafacil.facilino.common.LEDMatrix8x8Base;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBluetoothClient;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBluetoothActuator;

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
/**
 * A LED Matrix 8x8 component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A LED Matrix 8x8 component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/LED_matrix_24x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public class LEDMatrix8x8Bluetooth  extends LEDMatrix8x8Base implements FacilinoBluetoothActuator {
  /**
   * Creates a new Facilino component.
   */
  public LEDMatrix8x8Bluetooth(ComponentContainer container) {
    super(container); 
  }
  
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoBluetoothClient(FacilinoBluetoothClient facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}

  @Override
  @SimpleFunction(description = "Sends a telegram with a expression for a LED Matrix 8x8 to Facilino.")
  public void Show(long expression) {
		if (_facilino instanceof FacilinoBluetoothClient)
			((FacilinoBluetoothClient)_facilino).SendBytes(showTelegram(expression));
  }
  
  @Override
  @SimpleFunction(description = "Sends a number with the predefined expression for LED Matrix 8x8 telegram to Facilino.")
  public void ShowPredefExpression(byte number) {
	  if (_facilino instanceof FacilinoBluetoothClient)
			((FacilinoBluetoothClient)_facilino).SendBytes(showPredefExpressionTelegram(number));
  }
  
  
  private YailList showTelegram(long expression) {
	  byte[] bytes = new byte[15];
	  bytes[0]='@';
	  bytes[1]=FacilinoBluetoothClient.CMD_LED_MATRIX;
	  bytes[2]=11;
	  bytes[3]=(byte)_CLK_pin;
	  bytes[4]=(byte)_DIN_pin;
	  bytes[5]=(byte)_CS_pin;
	  bytes[6]=(byte)((expression>>52)&0xFF);
	  bytes[7]=(byte)((expression>>48)&0xFF);
	  bytes[8]=(byte)((expression>>40)&0xFF);
	  bytes[9]=(byte)((expression>>32)&0xFF);
	  bytes[10]=(byte)((expression>>24)&0xFF);
	  bytes[11]=(byte)((expression>>16)&0xFF);
	  bytes[12]=(byte)((expression>>8)&0xFF);
	  bytes[13]=(byte)(expression&0xFF);
	  bytes[14]='*';
	  int n=bytes.length;
	  Object[] array1 = new Object[n];
	  for (int i=0;i<n;i++)
		array1[i]=(Object)bytes[i];
	  YailList list = YailList.makeList(array1);
	  return list;
  }
  
  private YailList showPredefExpressionTelegram(byte number) {
	  byte[] bytes = new byte[8];
	  bytes[0]='@';
	  bytes[1]=FacilinoBluetoothClient.CMD_LED_MATRIX_PREDEF_EXPR;
	  bytes[2]=4;
	  bytes[3]=(byte)_CLK_pin;
	  bytes[4]=(byte)_DIN_pin;
	  bytes[5]=(byte)_CS_pin;
	  bytes[6]=number;
	  bytes[7]='*';
	  int n=bytes.length;
	  Object[] array1 = new Object[n];
	  for (int i=0;i<n;i++)
		array1[i]=(Object)bytes[i];
	  YailList list = YailList.makeList(array1);
	  return list;
  }

}
