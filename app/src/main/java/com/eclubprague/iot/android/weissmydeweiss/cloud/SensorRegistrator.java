package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * Created by paulos on 13. 7. 2015.
 */
public interface SensorRegistrator {
    @Get("json")
    public Sensor retrieve();

    @Post("json")
    public void store(Sensor sensor);

    @Post
    public String postik(String message);

    @Delete
    public void remove();
}
