// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package es.roboticafacil.facilino.runtime.bluetooth;

import java.util.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.BluetoothReflection;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.SdkLevel;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoBluetoothConnectionBase;
import com.google.appinventor.components.runtime.util.YailList;
import com.google.appinventor.components.runtime.util.TimerInternal;
import es.roboticafacil.facilino.runtime.bluetooth.FacilinoSensor;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * BluetoothClient component
 *
 * @author lizlooney@google.com (Liz Looney)
 */
@DesignerComponent(version = YaVersion.BLUETOOTHCLIENT_COMPONENT_VERSION,
    description = "Bluetooth client component",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "https://roboticafacil.es/facilino/blockly/img/ai2/facilino_logo_ai2_16x16.png")
@SimpleObject (external=true)
@UsesPermissions(permissionNames =
                 "android.permission.BLUETOOTH, " +
                 "android.permission.BLUETOOTH_ADMIN")
//@UsesLibraries(libraries = "es.roboticafacil.facilino.runtime.bluetooth.jar")
public final class FacilinoBluetoothClient extends FacilinoBluetoothConnectionBase {
  private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
  private final List<Component> attachedComponents = new ArrayList<Component>();
  private Set<Integer> acceptableDeviceClasses;
  private String _address;
  
  private TimerInternal timerInternal;
  private TinyDB db;
  
  public static byte CMD_DIGITAL_READ_REQ = 0x00;
	public static byte CMD_DIGITAL_READ_RESP = 0x01;
	public static byte CMD_DIGITAL_WRITE = 0x02;
	public static byte CMD_ANALOG_READ_REQ = 0x03;
	public static byte CMD_ANALOG_READ_RESP = 0x04;
	public static byte CMD_ANALOG_WRITE = 0x05;
	public static byte CMD_PUSH_BUTTON = 0x09;
	public static byte CMD_SERVO = 0x10;
	public static byte CMD_SERVO_CONT = 0x11;
	public static byte CMD_SONAR_READ_REQ = 0x12;
	public static byte CMD_SONAR_READ_RESP = 0x13;
	public static byte CMD_BUZZER_TONE = 0x20;
	public static byte CMD_BUZZER_MELODY = 0x21;
	//public static byte CMD_BUZZER_PREDEF_MELODY = 0x22;
	public static byte CMD_DHT_READ_REQ = 0x22;
	public static byte CMD_DHT_READ_RESP = 0x23;
	
	public static byte CMD_BOOLEAN_VAR = (byte)0x80;
	public static byte CMD_BOOLEAN_VAR_REQ = (byte)0x81;
	public static byte CMD_BOOLEAN_VAR_RESP = (byte)0x82;
	
	public static byte CMD_LED_MATRIX = 0x50;
	public static byte CMD_LED_MATRIX_PREDEF_EXPR = 0x51;
	public static byte CMD_LED_STRIP = 0x60;
	public static byte CMD_LED_STRIP_PREDEF = 0x61;
	public static byte CMD_LED_STRIP_SET_BRIGHTNESS = 0x62;
	
	private byte[] _telegramData = new byte[255];
	private byte _telegramLength = 0;
	private byte _telegramCmd = 0;
	private byte _telegramPos = 0;
  
  private static final int DEFAULT_INTERVAL = 100;  // ms
  private static final boolean DEFAULT_ENABLED = true;
  
  	private static final String ERROR_TELEGRAM="Error in bluetooth Telegram";

  /**
   * Creates a new BluetoothClient.
   */
  public FacilinoBluetoothClient(ComponentContainer container) {
    super(container, "BluetoothClient");
	timerInternal = new TimerInternal(this, DEFAULT_ENABLED, DEFAULT_INTERVAL);
	db = new TinyDB(container);
  }

  boolean attachComponent(Component component, Set<Integer> acceptableDeviceClasses) {
    if (attachedComponents.isEmpty()) {
      // If this is the first/only attached component, we keep the acceptableDeviceClasses.
      this.acceptableDeviceClasses = (acceptableDeviceClasses == null)
          ? null
          : new HashSet<Integer>(acceptableDeviceClasses);

    } else {
      // If there is already one or more attached components, the acceptableDeviceClasses must be
      // the same as what we already have.
      if (this.acceptableDeviceClasses == null) {
        if (acceptableDeviceClasses != null) {
          return false;
        }
      } else {
        if (acceptableDeviceClasses == null) {
          return false;
        }
        if (!this.acceptableDeviceClasses.containsAll(acceptableDeviceClasses)) {
          return false;
        }
        if (!acceptableDeviceClasses.containsAll(this.acceptableDeviceClasses)) {
          return false;
        }
      }
    }

    attachedComponents.add(component);
    return true;
  }

  void detachComponent(Component component) {
    attachedComponents.remove(component);
    if (attachedComponents.isEmpty()) {
      acceptableDeviceClasses = null;
    }
  }

  /**
   * Checks whether the Bluetooth device with the given address is paired.
   *
   * @param address the MAC address of the Bluetooth device
   * @return true if the device is paired, false otherwise
   */
  /*@SimpleFunction(description = "Checks whether the Bluetooth device with the specified address " +
  "is paired.")*/
  public boolean IsDevicePaired(String address) {
    String functionName = "IsDevicePaired";
    Object bluetoothAdapter = BluetoothReflection.getBluetoothAdapter();
    if (bluetoothAdapter == null) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_NOT_AVAILABLE);
      return false;
    }

    if (!BluetoothReflection.isBluetoothEnabled(bluetoothAdapter)) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_NOT_ENABLED);
      return false;
    }

    // Truncate the address at the first space.
    // This allows the address to be an element from the AddressesAndNames property.
    int firstSpace = address.indexOf(" ");
    if (firstSpace != -1) {
      address = address.substring(0, firstSpace);
    }

    if (!BluetoothReflection.checkBluetoothAddress(bluetoothAdapter, address)) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_INVALID_ADDRESS);
      return false;
    }

    Object bluetoothDevice = BluetoothReflection.getRemoteDevice(bluetoothAdapter, address);
    return BluetoothReflection.isBonded(bluetoothDevice);
  }

  /**
   * Returns the list of paired Bluetooth devices. Each element of the returned
   * list is a String consisting of the device's address, a space, and the
   * device's name.
   *
   * This method calls isDeviceClassAcceptable to determine whether to include
   * a particular device in the returned list.
   *
   * @return a List representing the addresses and names of paired
   *         Bluetooth devices
   */
  @SimpleProperty(description = "The addresses and names of paired Bluetooth devices",
      category = PropertyCategory.BEHAVIOR)
  public List<String> AddressesAndNames() {
    List<String> addressesAndNames = new ArrayList<String>();

    Object bluetoothAdapter = BluetoothReflection.getBluetoothAdapter();
    if (bluetoothAdapter != null) {
      if (BluetoothReflection.isBluetoothEnabled(bluetoothAdapter)) {
        for (Object bluetoothDevice : BluetoothReflection.getBondedDevices(bluetoothAdapter)) {
          if (isDeviceClassAcceptable(bluetoothDevice)) {
            String name = BluetoothReflection.getBluetoothDeviceName(bluetoothDevice);
            String address = BluetoothReflection.getBluetoothDeviceAddress(bluetoothDevice);
            addressesAndNames.add(address + " " + name);
          }
        }
      }
    }

    return addressesAndNames;
  }
  
  @SimpleProperty(description = "Returns the MAC address of the last connected device",
      category = PropertyCategory.BEHAVIOR)
	public String LastConnectedAddress() {
		return (String)db.GetValue("MAC","");
	}
  /**
   * Returns true if the class of the given device is acceptable.
   *
   * @param bluetoothDevice the Bluetooth device
   */
  private boolean isDeviceClassAcceptable(Object bluetoothDevice) {
    if (acceptableDeviceClasses == null) {
      // Add devices are acceptable.
      return true;
    }

    Object bluetoothClass = BluetoothReflection.getBluetoothClass(bluetoothDevice);
    if (bluetoothClass == null) {
      // This device has no class.
      return false;
    }

    int deviceClass = BluetoothReflection.getDeviceClass(bluetoothClass);
    return acceptableDeviceClasses.contains(deviceClass);
  }
  
  @SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description ="Interval between timer events in ms")
	public int TimerInterval() {
		return timerInternal.Interval();
	}

	/**
	 * Interval property setter method: sets the interval between timer events.
	 *
	 * @param interval timer interval in ms
	 */
	@DesignerProperty(
			editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
			defaultValue = DEFAULT_INTERVAL + "")
	@SimpleProperty
	public void TimerInterval(int interval) {
		timerInternal.Interval(interval);
	}
	
	/**
	 * Enabled property getter method.
	 *
	 * @return {@code true} indicates a running timer, {@code false} a stopped
	 *				 timer
	 */
	@SimpleProperty(
			category = PropertyCategory.BEHAVIOR,
			description = "Fires timer if true")
	public boolean TimerEnabled() {
		return timerInternal.Enabled();
	}

	/**
	 * Enabled property setter method: starts or stops the timer.
	 *
	 * @param enabled {@code true} starts the timer, {@code false} stops it
	 */
	@DesignerProperty(
			editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
			defaultValue = DEFAULT_ENABLED ? "True" : "False")
	@SimpleProperty
	public void TimerEnabled(boolean enabled) {
		timerInternal.Enabled(enabled);
	}

  /**
   * Connect to a Bluetooth device with the given address.
   *
   * @param address the MAC address of the Bluetooth device
   * @return true if the connection was successful, false otherwise
   */
  @SimpleFunction(description = "Connect to the Bluetooth device with the specified address and " +
      "the Serial Port Profile (SPP). Returns true if the connection was successful.")
  public boolean Connect(String address) {
	  _address=address;
    return connect("Connect", address, SPP_UUID);
  }

  /**
   * Connect to a Bluetooth device with the given address and a specific UUID.
   *
   * @param address the MAC address of the Bluetooth device
   * @param uuid the UUID
   * @return true if the connection was successful, false otherwise
   */
  /*@SimpleFunction(description = "Connect to the Bluetooth device with the specified address and " +
  "UUID. Returns true if the connection was successful.")
  public boolean ConnectWithUUID(String address, String uuid) {
    return connect("ConnectWithUUID", address, uuid);
  }*/

  /**
   * Connects to a Bluetooth device with the given address and UUID.
   *
   * If the address contains a space, the space and any characters after it
   * are ignored. This facilitates passing an element of the list returned from
   * the addressesAndNames method above.
   *
   * @param functionName the name of the SimpleFunction calling this method
   * @param address the address of the device
   * @param uuidString the UUID
   */
  private boolean connect(String functionName, String address, String uuidString) {
    Object bluetoothAdapter = BluetoothReflection.getBluetoothAdapter();
    if (bluetoothAdapter == null) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_NOT_AVAILABLE);
      return false;
    }

    if (!BluetoothReflection.isBluetoothEnabled(bluetoothAdapter)) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_NOT_ENABLED);
      return false;
    }

    // Truncate the address at the first space.
    // This allows the address to be an element from the AddressesAndNames property.
    int firstSpace = address.indexOf(" ");
    if (firstSpace != -1) {
      address = address.substring(0, firstSpace);
    }

    if (!BluetoothReflection.checkBluetoothAddress(bluetoothAdapter, address)) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_INVALID_ADDRESS);
      return false;
    }

    Object bluetoothDevice = BluetoothReflection.getRemoteDevice(bluetoothAdapter, address);
    if (!BluetoothReflection.isBonded(bluetoothDevice)) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_NOT_PAIRED_DEVICE);
      return false;
    }

    if (!isDeviceClassAcceptable(bluetoothDevice)) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_NOT_REQUIRED_CLASS_OF_DEVICE);
      return false;
    }

    UUID uuid;
    try {
      uuid = UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_INVALID_UUID, uuidString);
      return false;
    }

    Disconnect();

    try {
      connect(bluetoothDevice, uuid);
      return true;
    } catch (IOException e) {
      Disconnect();
      form.dispatchErrorOccurredEvent(this, functionName,
          ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_CONNECT);
      return false;
    }
  }

  private void connect(Object bluetoothDevice, UUID uuid) throws IOException {
    Object bluetoothSocket;
    if (!secure && SdkLevel.getLevel() >= SdkLevel.LEVEL_GINGERBREAD_MR1) {
      // createInsecureRfcommSocketToServiceRecord was introduced in level 10
      bluetoothSocket = BluetoothReflection.createInsecureRfcommSocketToServiceRecord(
          bluetoothDevice, uuid);
    } else {
      bluetoothSocket = BluetoothReflection.createRfcommSocketToServiceRecord(
          bluetoothDevice, uuid);
    }
    BluetoothReflection.connectToBluetoothSocket(bluetoothSocket);
    setConnection(bluetoothSocket);
    Log.i(logTag, "Connected to Bluetooth device " +
        BluetoothReflection.getBluetoothDeviceAddress(bluetoothDevice) + " " +
        BluetoothReflection.getBluetoothDeviceName(bluetoothDevice) + ".");
  }
  
  @SimpleFunction(description = "Forget last MAC address of the last connected device")
  public final void ForgetLastConnection() {
	  db.ClearAll();
  }
  
  @SimpleFunction(description = "Save current MAC address of the connected device")
  public final void SaveConnection() {
	  db.StoreValue("MAC",_address);
  }
  
  @SimpleFunction(description = "Reconnect with the last connected device using the last MAC address available")
  public final boolean Reconnect() {
	  _address=(String)db.GetValue("MAC","");
	  if (this.IsDevicePaired(_address) && !this.IsConnected())
	  {
		  return this.Connect(_address);
	  }
	  else
		  return false;
  }
  
  @Override
	public void alarm() {
		if (this.IsConnected())
		{
			while(this.BytesAvailableToReceive()>0)
			{
				List<Integer> bytes = this.ReceiveUnsignedBytes(this.BytesAvailableToReceive());
				processTelegram(bytes);
			}
		}
	}
	
	public void SendBytes(YailList list)
	{
		{
			if (this.IsConnected()){
				this.lock();
				this.SendBytes(list);
				this.unlock();
			}
		}
	}
	
	protected void processTelegram(List<Integer> bytes)
	{
		Iterator<Integer> bytesIterator = bytes.iterator(); 
		while (bytesIterator.hasNext()) {
			byte data = (byte)bytesIterator.next().intValue();
			if ((_telegramPos==0)&&(data=='@'))
				_telegramPos++;
			else if (_telegramPos==1)
			{
				_telegramCmd=data;
				_telegramPos++;
			}
			else if (_telegramPos==2)
			{
				_telegramLength=data;
				_telegramPos++;
			}
			else if ((_telegramPos>=3)&&(_telegramPos<(_telegramLength+3)))
			{
				_telegramData[_telegramPos-3]=data;
				_telegramPos++;
			}
			else if ((_telegramPos==(_telegramLength+3))&&(data=='*'))
			{
				//Here we have receive a successful telegram
				for (FacilinoSensor sensor: attachedSensors)
				{
					if (sensor instanceof FacilinoBluetoothSensor)
					{
						((FacilinoBluetoothSensor) sensor).dispatchData(_telegramCmd,_telegramData);
					}
				}
				_telegramPos=0;
			}
			else
			{
				TelegramError(FacilinoBluetoothClient.ERROR_TELEGRAM);
				_telegramPos=0;
			}
		}
	}
	
	@SimpleEvent(description = "Telegram error.")
	public void TelegramError(String error) {
			EventDispatcher.dispatchEvent(this, "TelegramError",error);
	}
}
