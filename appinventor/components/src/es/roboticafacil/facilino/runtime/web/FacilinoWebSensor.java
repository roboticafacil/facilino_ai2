// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.web;

import org.json.JSONObject;

interface FacilinoWebSensor {
	void FacilinoWeb(FacilinoWeb facilinoBase);
	void dispatchContents(JSONObject json);
}
