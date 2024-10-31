import { BluetoothPrinter } from 'bluetooth-printer';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    BluetoothPrinter.echo({ value: inputValue })
}
