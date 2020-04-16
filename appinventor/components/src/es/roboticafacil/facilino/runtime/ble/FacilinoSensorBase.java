// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.ble;

import com.google.appinventor.components.runtime.*;

import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.annotations.DesignerComponent;
import es.roboticafacil.facilino.runtime.ble.FacilinoBLEClient;


@DesignerComponent(version = Facilino.VERSION,
                   description = "A base class to all type of sensors.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true)
@SimpleObject (external =true)
public abstract class FacilinoSensorBase extends AndroidNonvisibleComponent
    implements Component, FacilinoSensor, Deleteable {
	protected FacilinoBase _ble;
	final protected String logTag;
	final protected byte _type;
	protected boolean _dataDispatched;
	protected int _updateTimeout;
	
	protected void BLEDevice(FacilinoBase ble) {
		_ble = ble;
		if (_ble!=null)
			_ble.attachSensor(this);
	}
	
	protected FacilinoSensorBase(ComponentContainer container, String logTag, byte type) {
		this(container.$form(), logTag, type);
	}
	
	private FacilinoSensorBase(Form form, String logTag, byte type) {
		super(form);
		this.logTag = logTag;
	_ble=null;
	_type=type;
	}
	
	protected void _attach() {
		if (_ble!=null)
		{
			_ble.detachSensor(this);
			_ble.attachSensor(this);
		}
		
	}
	
	protected void _detach() {
		if (_ble != null) {
		_ble.detachSensor(this);
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
	public byte sensorType()
	{
		return _type;
	}
	
	@Override
	public void onDelete() {
	}
	
	@SimpleProperty(description = "Update timeout in ms.",
									category = PropertyCategory.BEHAVIOR)
	public int UpdateTimeout() {
		return _updateTimeout;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
										defaultValue = "200")
	@SimpleProperty(description = "Update timeout in ms")
	public void UpdateTimeout(int updateTimeout) {
		_updateTimeout = updateTimeout;
	}
	
	@SimpleEvent(description = "Timeout error.")
	public void Timeout(String error) {
			EventDispatcher.dispatchEvent(this, "Timeout",error);
	}
}
