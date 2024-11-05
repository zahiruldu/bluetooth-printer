import { WebPlugin } from '@capacitor/core';

import type { BluetoothPrinterPlugin } from './definitions';

export class BluetoothPrinterWeb extends WebPlugin implements BluetoothPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async enableBluetooth(): Promise<void> {
    console.log('Enabling Bluetooth');
  }

  async getPairedDevices(): Promise<{ devices: any[] }> {
    return { devices: [] };
  }

  async connectToDevice(options: { macAddress: string }): Promise<void> {
    console.log('Connecting to device:', options);
  }

  async printText(options: { text: string }): Promise<void> {
    console.log('Printing text:', options.text);
  }
}
