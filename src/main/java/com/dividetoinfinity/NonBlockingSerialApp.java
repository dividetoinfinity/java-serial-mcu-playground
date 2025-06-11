package com.dividetoinfinity;

import com.fazecast.jSerialComm.SerialPort;

public class NonBlockingSerialApp {
    private static final SerialPort serial = SerialPort.getCommPorts()[0];

    public static void main(String[] args) {
        System.out.println(serial.toString());
        serial.openPort();
        try {
            for (;;) {
                if (serial.bytesAvailable() > 0) {
                    byte[] readBuffer = new byte[serial.bytesAvailable()];
                    int numRead = serial.readBytes(readBuffer, readBuffer.length);
                    System.out.println("Read " + numRead + " bytes.");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        serial.closePort();
    }

}