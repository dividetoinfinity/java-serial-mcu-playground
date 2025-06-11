package com.dividetoinfinity.device;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record IoTDevice(String mcu, String pin, boolean led) {

}
