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
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.SdkLevel;
import es.roboticafacil.facilino.common.FacilinoBase;
import es.roboticafacil.facilino.common.Facilino;
import com.google.appinventor.components.runtime.util.YailList;
//import java.lang.Class;
import java.lang.reflect.*;
import java.util.Set;
import org.json.JSONObject;
/**
 * A sonar component that provides a low-level interface to Facilino
 * with functions to send direct commands/telegrams to Facilino.
 *
 * @author Leopoldo Armesto soporte@roboticafacil.es
 */
@DesignerComponent(version = Facilino.VERSION,
                   description = "A sonar component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/hc_sr04_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public abstract class SonarBase extends FacilinoSensorBase {
  protected byte _pin_ECHO;
  protected byte _pin_TRIGGER;
  protected int _distance;
  protected boolean _dataDispatched;
  protected int _threshold;
  
  /**
   * Creates a new Facilino component.
   */
  public SonarBase(ComponentContainer container) {
	  super(container,"Sonar",FacilinoBase.TYPE_SONAR);
	  _dataDispatched=false;
  }
  
  @SimpleProperty(category = PropertyCategory.BEHAVIOR)
  public byte Echo() {
    return _pin_ECHO;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
                    defaultValue = "7")
  @SimpleProperty(description = "The ECHO pin")
  public void Echo(byte pin) {
    _pin_ECHO = pin;
  }
  
  @SimpleProperty(category = PropertyCategory.BEHAVIOR)
  public byte Trigger() {
    return _pin_TRIGGER;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
                    defaultValue = "12")
  @SimpleProperty(description = "The TRIGGER pin")
  public void Trigger(byte pin) {
    _pin_TRIGGER = pin;
  }
  
  @SimpleProperty(category = PropertyCategory.BEHAVIOR)
  public int Threshold() {
    return _threshold;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
                    defaultValue = "20")
  @SimpleProperty(description = "Threshold value to compare, if distance is smaller than threshold then throws collision event")
  public void Threshold(int threshold) {
    _threshold = threshold;
  }
  
  @SimpleProperty(description = "The measured distance (last received).",
                  category = PropertyCategory.BEHAVIOR)
  public int Distance() {
    return _distance;
  }
    
  @SimpleFunction(description = "Sends a sonar read request to Facilino and waits for response.")
  public abstract void Update() throws InterruptedException;
  
  @SimpleFunction(description = "Sends a sonar read request to Facilino.")
  public abstract void Request();
  
	
	@SimpleEvent(description = "Sonar distance read (in cm) event.")
    public void Received(int distance){
        EventDispatcher.dispatchEvent(this, "Received",distance);
    }
	
	@SimpleEvent(description = "Sonar collision event when the distance is lower than the threshold.")
    public void Collision(){
        EventDispatcher.dispatchEvent(this, "Collision");
    }

}
