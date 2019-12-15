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
 * A RGB LED Strip component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A RGB LED Strip component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/led_strip_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class RGB_LEDStripBase  extends FacilinoActuatorBase {

  protected byte _pin;
 
  /**
   * Creates a new Facilino component.
   */
  public RGB_LEDStripBase(ComponentContainer container) {
    super(container.$form(),"RGB_LEDStrip",FacilinoBase.TYPE_LED_STRIP);
	_pin=3;
  }
  
  @SimpleProperty(description = "The RGB LED Strip pin.",
                  category = PropertyCategory.BEHAVIOR)
  public byte Pin() {
    return _pin;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
                    defaultValue = "2")
  @SimpleProperty (description = "Pin of the IN input of the LED strip")
  public void Pin(byte pin) {
    _pin = pin;
  }
  
  @SimpleFunction(description = "Sends a RGB_LEDs strip telegram to Facilino.")
  public void SetLEDs(YailList colors) {};
  
  @SimpleFunction(description = "Sends a number with the predefined RGB LED strip telegram to Facilino.")
  public void ShowPredefLEDs(int number) {};
  
  @SimpleFunction(description = "Sends a number with the predefined RGB LED strip telegram to Facilino.")
  public void SetBrightness(int number) {};

}
