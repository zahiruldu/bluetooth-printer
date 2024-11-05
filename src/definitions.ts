export interface BluetoothPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  enableBluetooth(): Promise<void>;
  getPairedDevices(): Promise<{ devices: any[] }>;
  connectToDevice(options: { macAddress: string }): Promise<void>;
  printText(options: { text: string }): Promise<void>;
}
