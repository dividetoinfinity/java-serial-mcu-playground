package com.dividetoinfinity;

import com.dividetoinfinity.device.IoTDevice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SemiBlockingStreamSerialApp {
    private static SerialPort serial;
    private static Runnable serialRead;
    private static Runnable serialWrite;
    private static IoTDevice device;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ObjectMapper objectMapper;
    private static String jsonString;
    private static InputStream in;
    private static OutputStream out;

    private SemiBlockingStreamSerialApp() {
        initialise();
        loop();
    }

    private void initialise() {
        // Mock MCU device object state
        device = new IoTDevice("Arduino Uno R3", "PIN_13", true);
        objectMapper = new ObjectMapper();
        jsonString = "";
        serial = SerialPort.getCommPorts()[0];
        System.out.println("\n" + serial.getPortDescription());

        serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        serial.setBaudRate(BaudRate.BAUD_RATE_9600.getBaudRate());
        serial.openPort();

        serialRead = () -> {

            System.out.println("Serial InputStream read: " + Instant.now());

            if (serial.isOpen()) {
                System.out.println("Serial port is open");
            } else {
                System.out.println("Serial port is closed");
            }

            try {
                if (in.read() == -1) {
                    System.out.println("Serial port is closed");
                    System.out.println("No input to read");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // MCU signalling that the last buffer has been received
            // and computation has completed
            // If the MCU state has updated, the next buffer of JSON over to the MCU
            try {
                // ASCII character 'A' received by itself (JSON) is the command for ASync
                if (in.read() == 65) {
                    System.out.println("Received an 'A' from MCU");
                }
                // TODO: Read in the contents from the MCU buffer, then filter JSON and parse
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scheduler.schedule(serialWrite, 20, TimeUnit.MILLISECONDS);
        };

        serialWrite = () -> {
            System.out.println("Serial OutputStream write: " + Instant.now());
            try {
                // TODO: If the device state is the same as the last update, fall through
                jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(device);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            System.out.println(jsonString);
            byte[] buffer = jsonString.getBytes(StandardCharsets.UTF_8);

            try {
                out.write(buffer, 0, buffer.length);
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scheduler.schedule(serialRead, 10, TimeUnit.MILLISECONDS);
        };

        if (serial.isOpen()) {
            System.out.println("Output on Serial port " +
                    serial.getSystemPortName() + " with baud rate: " +
                    serial.getBaudRate() + " " +
                    "open and ready to receive communication: "
            );
            scheduler.schedule(serialRead, 10, TimeUnit.MILLISECONDS);
        }

        in = serial.getInputStream();
        out = serial.getOutputStream();

    }

    private void loop() {
        // while (true) {}
    }

    private void destroy() {
        serial.closePort();
        scheduler.shutdown();
    }

    public static void main(String[] args) {
        new SemiBlockingStreamSerialApp();
    }

}
