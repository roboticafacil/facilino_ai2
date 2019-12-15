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

import es.roboticafacil.facilino.runtime.web.Facilino;
import es.roboticafacil.facilino.runtime.web.FacilinoBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;
import es.roboticafacil.facilino.runtime.web.LEDMatrix8x8Base;
import es.roboticafacil.facilino.runtime.web.FacilinoWebActuator;

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
public class LEDMatrix8x8Web  extends LEDMatrix8x8Base implements FacilinoWebActuator {
  /**
   * Creates a new Facilino component.
   */
  public LEDMatrix8x8Web(ComponentContainer container) {
    super(container);
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoWeb(FacilinoWeb facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}
	
  
  @SimpleFunction(description = "Sends a telegram with a expression for a LED Matrix 8x8 to Facilino.")
  public void ShowCustomExpression(int col1, int col2, int col3, int col4, int col5, int col6, int col7, int col8) {
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURLExpr(col1,col2,col3,col4,col5,col6,col7,col8));
  }
  
  @SimpleFunction(description = "Sends a number with the predefined expression for LED Matrix 8x8 telegram to Facilino.")
  public void ShowPredefExpression(byte number) {
	  if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURLPredefExpr(number));
  }
  
  private String buildURLExpr(int col1, int col2, int col3, int col4, int col5, int col6, int col7, int col8)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_CLK_pin;
		str+="_";
		str+=_DIN_pin;
		str+="_";
		str+=_CS_pin;
		str+="?c1=";
		str+=col1;
		str+="&c2=";
		str+=col2;
		str+="&c3=";
		str+=col3;
		str+="&c4=";
		str+=col4;
		str+="&c5=";
		str+=col5;
		str+="&c6=";
		str+=col6;
		str+="&c7=";
		str+=col7;
		str+="&c8=";
		str+=col8;
		return str;
	}
	
	private String buildURLPredefExpr(byte number)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_CLK_pin;
		str+="_";
		str+=_DIN_pin;
		str+="_";
		str+=_CS_pin;
		str+="?number=";
		str+=number;
		return str;
	}

}
