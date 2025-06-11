package com.dividetoinfinity;

import com.fazecast.jSerialComm.SerialPort;

public class SemiBlockingKnownBufferSizeSerial {
    private static final SerialPort serial = SerialPort.getCommPorts()[0];

    public static void main(String[] args) {
        serial.openPort();
        // TIMEOUT_READ_BLOCKING ensures readBytes() has no return until readBuffer.length is full
        serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
        try {
            for (;;) {
                byte[] readBuffer = new byte[1024];
                int numRead = serial.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        serial.closePort();
    }

}
