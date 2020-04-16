// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2018 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.ble;

import android.Manifest;
import android.os.Build;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.PermissionResultHandler;

/**
 * Helper class to handle the transition from targetSdk 7 to targetSdk 26 and the change in permission
 * model that Google implemented in Android 6.0 Marshmallow.
 *
 * @author ewpatton@mit.edu (Evan W. Patton)
 */
class SDK26Helper {

  static boolean shouldAskForPermission(Form form) {
    return form.getApplicationInfo().targetSdkVersion >= 23 &&
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
  }

  static void askForPermission(final FacilinoBLEClient ble, final Runnable next) {
    ble.getForm().askPermission(Manifest.permission.ACCESS_COARSE_LOCATION, new PermissionResultHandler() {
      @Override
      public void HandlePermissionResponse(String permission, boolean granted) {
        if (granted) {
          next.run();
        } else {
          ble.getForm().PermissionDenied(ble, "StartScanning", permission);
        }
      }
    });
  }
}
