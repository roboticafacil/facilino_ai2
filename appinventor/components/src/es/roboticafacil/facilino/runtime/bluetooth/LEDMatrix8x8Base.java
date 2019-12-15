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

import es.roboticafacil.facilino.runtime.bluetooth.Facilino;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBase;

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
                   description = "A 8x8 LED matrix component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/led_strip_16x16.png")
@SimpleObject (external=true)
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

  public abstract void ShowCustomExpression(byte col1, byte col2, byte col3, byte col4, byte col5, byte col6, byte col7, byte col8);
  
  @SimpleFunction(description = "Sends a number with the predefined expression for LED Matrix 8x8 telegram to Facilino.")
  public abstract void ShowPredefExpression(byte number);
}
