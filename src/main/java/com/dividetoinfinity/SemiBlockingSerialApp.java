package com.dividetoinfinity;

import com.fazecast.jSerialComm.SerialPort;

public class SemiBlockingSerialApp {
    private static final SerialPort serial = SerialPort.getCommPorts()[0];

    public static void main(String[] args) {
        serial.openPort();
        serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
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
