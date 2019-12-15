// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.common;

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
  protected FacilinoBase _facilino;
  final protected String logTag;
  final protected byte _type;
  
  
  protected void FacilinoDevice(FacilinoBase facilinoBase) {
    _facilino = facilinoBase;
	if (_facilino!=null)
	  _facilino.attachActuator(this);
  }
  
  protected FacilinoActuatorBase(ComponentContainer container, String logTag, byte type) {
    this(container.$form(), logTag, type);
  }
  
  private FacilinoActuatorBase(Form form, String logTag, byte type) {
    super(form);
    this.logTag = logTag;
	_facilino=null;
	_type=type;
  }
  
  protected void _attach() {
	  if (_facilino!=null)
	  {
		  _facilino.detachActuator(this);
		  _facilino.attachActuator(this);
	  }
	  
  }
  
  protected void _detach() {
	  if (_facilino != null) {
		_facilino.detachActuator(this);
		_facilino = null;
	  }
  }
  
  protected void attach(FacilinoBase facilino) {
		_facilino=facilino;
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
