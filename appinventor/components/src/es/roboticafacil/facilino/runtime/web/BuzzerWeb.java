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
import es.roboticafacil.facilino.common.Facilino;
import es.roboticafacil.facilino.common.FacilinoBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWeb;
import es.roboticafacil.facilino.common.BuzzerBase;
import es.roboticafacil.facilino.runtime.web.FacilinoWebActuator;

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
                   description = "A buzzer component that provides a low-level interface to Facilino " +
                                 "with functions to send direct commands/telegrams to Facilino.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/buzzer_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames = "android.permission.INTERNET," +
                                   "android.permission.WRITE_EXTERNAL_STORAGE," +
                                   "android.permission.READ_EXTERNAL_STORAGE")
public class BuzzerWeb  extends BuzzerBase {
	/**
	 * Creates a new Facilino component.
	 */
	public BuzzerWeb(ComponentContainer container) {
		super(container);
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT,
										defaultValue = "")
	@SimpleProperty
	public void FacilinoWeb(FacilinoWeb facilinoBase) {
		this.FacilinoDevice(facilinoBase);
	}
	
	@SimpleFunction(description = "Sends a tone telegram to Facilino.")
	public void Tone(int frequency, int duration) {
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURLTone(frequency,duration));
	}
	
	private String buildURLTone(int frequency, int duration)
	{
		String str="/";
		str+=logTag;
		str+="/";
		str+=_pin;
		str+="?freq=";
		str+=frequency;
		str+="&dur=";
		str+=duration;
		return str;
	}
	
	/*@SimpleFunction(description = "Sends a number with the predefined melody telegram to Facilino.")
	public void PredefMelody(byte number) {
		_facilino.SendBytes(predefMelodyTelegram(number));
	}*/
	
	@SimpleFunction(description = "Sends a melody telegram to Facilino.")
	public void Melody(YailList melody) {
		if (_facilino instanceof FacilinoWeb)
			((FacilinoWeb)_facilino).GetURL(buildURLMelody(melody));
	}
	
	private String buildURLMelody(YailList melody)
	{
		int n;
		Object[] array = melody.toArray();
		String str="/";
		str+=logTag;
		str+="/";
		str+=_pin;
		str+="?melody=";
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
}
