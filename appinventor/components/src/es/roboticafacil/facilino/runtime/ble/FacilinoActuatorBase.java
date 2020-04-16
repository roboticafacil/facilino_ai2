// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.ble;

import com.google.appinventor.components.runtime.*;

import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.SimpleObject;

//@SimpleObject (external =true)
public abstract class FacilinoActuatorBase extends AndroidNonvisibleComponent
    implements Component, Deleteable, FacilinoActuator {
  /**
   *
   */
  protected FacilinoBase _ble;
  final protected String logTag;
  final protected byte _type;
  
  
  protected void BLEDevice(FacilinoBase ble) {
    _ble = ble;
	if (_ble!=null)
	  _ble.attachActuator(this);
  }
  
  protected FacilinoActuatorBase(ComponentContainer container, String logTag, byte type) {
    this(container.$form(), logTag, type);
  }
  
  private FacilinoActuatorBase(Form form, String logTag, byte type) {
    super(form);
    this.logTag = logTag;
	_ble=null;
	_type=type;
  }
  
  protected void _attach() {
	  if (_ble!=null)
	  {
		  _ble.detachActuator(this);
		  _ble.attachActuator(this);
	  }
	  
  }
  
  protected void _detach() {
	  if (_ble != null) {
		_ble.detachActuator(this);
		_ble = null;
	  }
  }
  
  protected void attach(FacilinoBase ble) {
		_ble=ble;
		_attach();
	}
	
	protected void detach() {
		_detach();
	}
  
  @Override
	public byte actuatorType()
	{
		return _type;
	}
	
@Override
  public void onDelete() {
	_detach();
  }
}
