package com.dividetoinfinity;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
// This method can only be used with Windows
public class EventBasedWriteApp {
    private static SerialPort comPort = SerialPort.getCommPorts()[0];

    public static void main(String[] args) {
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_WRITTEN;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN) {
                    System.out.println("All bytes were successfully transmitted!");
                }
            }
        });
    }
}
