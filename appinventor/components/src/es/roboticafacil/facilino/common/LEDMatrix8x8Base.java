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

//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
/**
 * A LED Matrix 8x8 component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
//@SimpleObject (external =true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class LEDMatrix8x8Base  extends FacilinoActuatorBase {
  
  protected byte _CS_pin;
  protected byte _DIN_pin;
  protected byte _CLK_pin;
  /**
   * Creates a new Facilino component.
   */
  public LEDMatrix8x8Base(ComponentContainer container) {
    super(container,"LEDMatrix8x8",FacilinoBase.TYPE_LED_MATRIX); 
	_CS_pin=10;
	_DIN_pin=12;
	_CLK_pin=11;
  }
  
  @SimpleProperty(description = "CS pin of LED Matrix.",
                  category = PropertyCategory.BEHAVIOR)
  public byte CS() {
    return _CS_pin;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
                    defaultValue = "10")
  @SimpleProperty(description = "CS pin of LED Matrix")
  public void CS(byte pin) {
    _CS_pin = pin;
  }
  
  @SimpleProperty(description = "DIN pin of LED Matrix.",
                  category = PropertyCategory.BEHAVIOR)
  public byte DIN() {
    return _DIN_pin;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
                    defaultValue = "12")
  @SimpleProperty(description = "DIN pin of LED Matrix")
  public void DIN(byte pin) {
    _DIN_pin = pin;
  }
  
  @SimpleProperty(description = "CLK pin of LED Matrix.",
                  category = PropertyCategory.BEHAVIOR)
  public byte CLK() {
    return _CLK_pin;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
                    defaultValue = "11")
  @SimpleProperty(description = "CLK pin of LED Matrix")
  public void CLK(byte pin) {
    _CLK_pin = pin;
  }

  @SimpleFunction(description = "Sends a telegram with a expression for a LED Matrix 8x8 to Facilino.")
  public void Show(long expression) {};
  
  @SimpleFunction(description = "Sends a number with the predefined expression for LED Matrix 8x8 telegram to Facilino.")
  public void ShowPredefExpression(byte number) {};
  
  @SimpleFunction(description = "Converts rows into an expression")
  public long ToExpression(byte col1, byte col2, byte col3, byte col4, byte col5, byte col6, byte col7, byte col8) {
	  long _col1=col1;
	  long _col2=col2;
	  long _col3=col3;
	  long _col4=col4;
	  long _col5=col5;
	  long _col6=col6;
	  long _col7=col7;
	  long _col8=col8;
	  long expression=((_col1<<52)&0x00000000000000FF)|((_col2<<48)&0x00000000000000FF)|((_col3<<40)&0x00000000000000FF)|((_col4<<32)&0x00000000000000FF)|((_col5<<24)&0x00000000000000FF)|((_col6<<16)&0x00000000000000FF)|((_col7<<8)&0x00000000000000FF)|(_col8&0x00000000000000FF);
	  return expression;
  }
}
