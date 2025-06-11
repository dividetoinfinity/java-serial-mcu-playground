package com.dividetoinfinity;

public enum BaudRate {
    BAUD_RATE_9600(9600),
    BAUD_RATE_115200(115200);

    private final Integer baudRate;

    BaudRate(Integer baudRate) {
        this.baudRate = baudRate;
    }

    public Integer getBaudRate() {
        return baudRate;
    }
}
