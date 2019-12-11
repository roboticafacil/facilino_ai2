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
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.SdkLevel;
import es.roboticafacil.facilino.common.Facilino;
import es.roboticafacil.facilino.common.FacilinoBase;
import es.roboticafacil.facilino.common.SonarBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWebSensor;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;
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
public class SonarWeb extends SonarBase implements FacilinoWebSensor {
  /**
   * Creates a new Facilino component.
   */
  public SonarWeb(ComponentContainer container) {
	  super(container);
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoWeb(FacilinoWeb facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}
	
  @SimpleFunction(description = "Sends a sonar read request to Facilino and waits for response.")
  public void Update() throws InterruptedException {
	  _dataDispatched=false;
	  if (_facilino instanceof FacilinoWeb)
	  {
		((FacilinoWeb)_facilino).GetURL(buildURL());
		while (!_dataDispatched){Thread.sleep(1);}
	  }
  }
  
  @SimpleFunction(description = "Sends a sonar read request to Facilino.")
  public void Request() {
	  _dataDispatched=false;
	  if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURL());
  }
  
  private String buildURL()
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_pin_ECHO;
		str+="_";
		str+=_pin_TRIGGER;
		return str;
	}

  public void dispatchContents(JSONObject json)
  {
	  _dataDispatched=true;
  }

}
