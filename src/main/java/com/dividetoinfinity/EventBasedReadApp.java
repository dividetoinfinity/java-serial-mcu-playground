package com.dividetoinfinity;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class EventBasedReadApp {
    private static SerialPort comPort = SerialPort.getCommPorts()[0];

    public static void main(String[] args) {
        comPort.openPort();

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                System.out.println("Read " + numRead + " bytes");
            }
        });
    }
}
