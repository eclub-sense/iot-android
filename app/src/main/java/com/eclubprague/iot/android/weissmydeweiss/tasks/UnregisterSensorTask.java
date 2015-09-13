package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.ShareSensorEntity;

import org.restlet.resource.ClientResource;

/**
 * Created by Dat on 9.9.2015.
 */
public class UnregisterSensorTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        try {
            ClientResource resource = new ClientResource("http://mlha-139.sin.cvut.cz:8080/registered_sensors/" + params[0]);
            resource.setQueryValue("access_token", params[1]);

            RegisteredSensors sr = resource.wrap(RegisteredSensors.class);
            Log.e("UNREGISTERING", "...");
            sr.remove();
        } catch (Exception e) {
            Log.e("UnregisterSensorTask", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}