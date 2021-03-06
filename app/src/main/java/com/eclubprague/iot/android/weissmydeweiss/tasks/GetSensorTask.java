package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;

import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.SensorRegistrator;
import com.eclubprague.iot.android.weissmydeweiss.cloud.gsonmods.GsonCustomConverter;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.resource.ClientResource;

import java.lang.ref.WeakReference;

/**
 * Refreshes sensor list from the server based on hub ID.
 */
public class GetSensorTask extends AsyncTask<String, Void, GetSensorTask.GetSensorResult> {
    private final WeakReference<GetSensorCallbacks> callbacks;

    public GetSensorTask(GetSensorCallbacks callbacks) {
        this.callbacks = new WeakReference<GetSensorCallbacks>(callbacks);
    }

    public interface GetSensorCallbacks {
        void handleGetSensorResult(GetSensorResult sensorResult);
    }

    public class GetSensorResult {
        public GetSensorResult(boolean success, Sensor sensor) {
            this.success = success;
            this.sensor = sensor;
        }

        boolean success;
        Sensor sensor;

        public boolean isSuccess() {
            return success;
        }

        public Sensor getSensor() {
            return sensor;
        }
    }

    @Override
    protected GetSensorResult doInBackground(String... uuids) {
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().getRegisteredConverters().add(new GsonCustomConverter());

        try {
            // try connection
            ClientResource cr = new ClientResource("http://zettor.sin.cvut.cz:8080/registered_sensors/" + uuids[0]);
            RegisteredSensors rs = cr.wrap(RegisteredSensors.class);

            return new GetSensorResult(true, rs.get(uuids[0]));
        } catch(Throwable thr) {
            thr.printStackTrace();
        }

        return new GetSensorResult(false, null);
    }

    @Override
    protected void onPostExecute(GetSensorResult sensorResult) {
        super.onPostExecute(sensorResult);

        GetSensorCallbacks rsc = callbacks.get();
        if(rsc != null) {
            rsc.handleGetSensorResult(sensorResult);
        }
    }
}
