package com.hellosofts.plugins.bluetoothprinter;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.bluetooth.BluetoothAdapter;
import com.getcapacitor.annotation.Permission;
import android.Manifest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


@CapacitorPlugin(
    name = "BluetoothPrinter",
    permissions = {
        @Permission(
                alias = "bluetooth",
                strings = {Manifest.permission.BLUETOOTH}
        ),
        @Permission(
                alias = "bluetooth_admin",
                strings = {Manifest.permission.BLUETOOTH_ADMIN}
        )
    }
)
public class BluetoothPrinterPlugin extends Plugin {

    private BluetoothPrinter implementation = new BluetoothPrinter();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void enableBluetooth(PluginCall call) {
        implementation.enableBluetooth();
        call.resolve();
    }

    @PluginMethod
    public void getPairedDevices(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("devices", implementation.getPairedDevices());
        call.resolve(ret);
    }

    private BluetoothSocket bluetoothSocket;
    private static final String LOG_TAG = "BluetoothPrinter";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SPP UUID

    @PluginMethod
    public void connectToDevice(PluginCall call) {
        String macAddress = call.getString("macAddress");

        if (macAddress == null) {
            call.reject("MAC address is required to connect to a Bluetooth device.");
            return;
        }

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            call.reject("Bluetooth is not supported on this device.");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            call.reject("Bluetooth is disabled. Please enable Bluetooth and try again.");
            return;
        }

        // Get the Bluetooth device
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);

        try {
            // Establish a Bluetooth socket connection
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            Log.d(LOG_TAG, "Connected to device " + macAddress);

            // Resolve the call if the connection is successful
            call.resolve();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to device: " + e.getMessage());
            call.reject("Failed to connect to device: " + e.getMessage());

            // Close the socket if an exception occurred
            try {
                if (bluetoothSocket != null) {
                    bluetoothSocket.close();
                }
            } catch (IOException closeException) {
                Log.e(LOG_TAG, "Error closing socket after failed connection: " + closeException.getMessage());
            }
        }
    }

    @PluginMethod
    public void printText(PluginCall call) {
        String text = call.getString("text");
    if (text == null) {
        call.reject("Text to print is required.");
        return;
    }

    if (bluetoothSocket == null || !bluetoothSocket.isConnected()) {
        call.reject("Printer is not connected.");
        return;
    }

    try {
        OutputStream outputStream = bluetoothSocket.getOutputStream();

        // Initialize the printer (reset command for some printers)
        outputStream.write(new byte[]{0x1B, 0x40}); // ESC @ (Initialize/reset printer)

        // Format text for 57mm paper width
        String formattedText = implementation.formatTextFor57mm(text);

        // Send text to printer
        outputStream.write(formattedText.getBytes("GBK")); // Adjust encoding as needed

        // Line feed for spacing
        outputStream.write(new byte[]{0x0A}); // Newline character

        // Feed paper after printing
        outputStream.write(new byte[]{0x1D, 0x56, 0x41, 0x10}); // ESC i (cut and feed paper)

        outputStream.flush();
        call.resolve();
    } catch (IOException e) {
        call.reject("Failed to print text: " + e.getMessage());
    }
    }
}
