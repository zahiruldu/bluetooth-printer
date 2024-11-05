package com.hellosofts.plugins.bluetoothprinter;

import android.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import java.util.Set;
import android.annotation.SuppressLint;
import java.nio.charset.StandardCharsets;

public class BluetoothPrinter {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    public void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.i("Bluetooth", "Bluetooth enabled");

        // enable bluetooth if not enabled
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.i("Bluetooth", "Bluetooth is not supported on this device.");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            // Enable Bluetooth
            bluetoothAdapter.enable();
            Log.i("Bluetooth", "Bluetooth enabled");
        } else {
            Log.i("Bluetooth", "Bluetooth was already enabled");
        }

    }

    @SuppressLint("MissingPermission")
    public JSArray getPairedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        JSArray devices = new JSArray();
        JSObject deviceObject = new JSObject();

        Log.d("Bluetooth paired devices", pairedDevices.toString());

        for (BluetoothDevice device : pairedDevices) {
            
            deviceObject.put("name", device.getName());
            deviceObject.put("address", device.getAddress());
            devices.put(deviceObject);
        }

        return devices;
    }

    public String formatTextFor57mm(String text) {
        int maxCharsPerLine = 32; // Adjust based on font size and printer settings
        StringBuilder formattedText = new StringBuilder();
    
        // Split text into lines based on maxCharsPerLine
        int index = 0;
        while (index < text.length()) {
            if (index + maxCharsPerLine <= text.length()) {
                formattedText.append(text.substring(index, index + maxCharsPerLine));
            } else {
                formattedText.append(text.substring(index));
            }
            formattedText.append("\n"); // Newline after each line
            index += maxCharsPerLine;
        }
        return formattedText.toString();
    }
}
