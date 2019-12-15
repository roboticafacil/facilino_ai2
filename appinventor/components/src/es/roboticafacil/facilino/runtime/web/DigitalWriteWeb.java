// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2016 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.web;

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
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.Ev3BinaryParser;
import com.google.appinventor.components.runtime.util.Ev3Constants;
import com.google.appinventor.components.runtime.util.SdkLevel;

import com.google.appinventor.components.runtime.util.YailList;
import es.roboticafacil.facilino.runtime.web.Facilino;
import es.roboticafacil.facilino.runtime.web.FacilinoBase;
import es.roboticafacil.facilino.runtime.web.DigitalWriteBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWebActuator;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;

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
                   description = "A digital output (write) component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/digital_signal_out_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public class DigitalWriteWeb  extends DigitalWriteBase implements FacilinoWebActuator {
  
  /**
   * Creates a new Facilino component.
   */
  public DigitalWriteWeb(ComponentContainer container) {
    super(container);
  }
  
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoWeb(FacilinoWeb facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}

  @SimpleFunction(description = "Sets a digital output.")
  public void Set(boolean value) {
	  _value=value;
	  if (_facilino instanceof FacilinoWeb)
		((FacilinoWeb)_facilino).GetURL(buildURL(value));
  }
  
  @SimpleFunction(description = "Toggles a digital output by sending a telegram to Facilino.")
  public void Toggle() {
	  _value=!_value;
	  if (_facilino instanceof FacilinoWeb)
		((FacilinoWeb)_facilino).GetURL(buildURL(_value));
  }

  private String buildURL(boolean value)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_pin;
		str+="?value=";
		str+=value? "1" : "0";
		return str;
	}
}
