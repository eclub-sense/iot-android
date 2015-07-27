package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

public enum SensorType {
	THERMOMETER(0x41, "thermometer"), // 0x41
	LED(0x1, "led"),
	GPS(0x42, "gps");

	private final int code;
	private final String name;

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	private SensorType(int code, String name) {
		this.code = code;
		this.name = name;
	}
}
