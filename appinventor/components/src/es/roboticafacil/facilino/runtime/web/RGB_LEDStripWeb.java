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
import com.google.appinventor.components.runtime.util.SdkLevel;

import com.google.appinventor.components.runtime.util.YailList;
import es.roboticafacil.facilino.common.Facilino;
import es.roboticafacil.facilino.common.FacilinoBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;
import es.roboticafacil.facilino.common.RGB_LEDStripBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWebActuator;

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
public class RGB_LEDStripWeb extends RGB_LEDStripBase implements FacilinoWebActuator { 
  /**
   * Creates a new Facilino component.
   */
  public RGB_LEDStripWeb(ComponentContainer container) {
    super(container);
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoWeb(FacilinoWeb facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}
  
  @SimpleFunction(description = "Sends a RGB_LEDs strip telegram to Facilino.")
  public void SetLEDs(YailList colors) {
	  if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURLLEDs(colors));
  }
  
  private String buildURLLEDs(YailList colors)
	{
		int n;
		Object[] array = colors.toArray();
		String str="/";
		str+=logTag;
		str+="/";
		str+=_pin;
		str+="?colors=";
		for (int i = 0; i < (array.length-1); i++)
		{
			Object el = array[i];
			String s = el.toString();
			try {
			n = Integer.decode(s);
			} catch (NumberFormatException e) {
				System.out.println(e.toString());
			return "";
			}
			str+=n;
			str+=",";
		}
		Object el = array[array.length-1];
		String s = el.toString();
		try {
		n = Integer.decode(s);
		} catch (NumberFormatException e) {
			System.out.println(e.toString());
		return "";
		}
		str+=n;
		return str;
	}
  
  @SimpleFunction(description = "Sends a number with the predefined RGB LED strip telegram to Facilino.")
  public void ShowPredefLEDs(int number) {
	  if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURLPredefLEDs(number));
  }
  
  private String buildURLPredefLEDs(int number)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_pin;
		str+="?number=";
		str+=number;
		return str;
	}
  
  @SimpleFunction(description = "Sends a number with the predefined RGB LED strip telegram to Facilino.")
  public void SetBrightness(int number) {
	  if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURLBrightness(number));
  }
  
  private String buildURLBrightness(int number)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_pin;
		str+="?brightness=";
		str+=number;
		return str;
	}
}
