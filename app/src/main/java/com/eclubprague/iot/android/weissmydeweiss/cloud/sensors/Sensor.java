package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.data.SensorData;

public class Sensor {

	private int uuid;
	private SensorType type;
	private int secret;
	private SensorData data;
	
	public Sensor(int uuid, SensorType type, int secret) {
		this.uuid = uuid;
		this.type = type;
		this.secret = secret;
	}

	public void setData(SensorData data) {
		this.data = data;
	}
	
	public SensorData getData() {
		return data;
	}
	
	@Override
	public String toString() {
		return "Sensor [uuid=" + uuid + ", type=" + type + ", secret=" + secret + "]";
	}
}