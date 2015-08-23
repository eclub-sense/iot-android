package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.MainActivity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.RegisteredSensorsMessage;

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 13.8.2015.
 */
public class GetSensorDataByIdTask extends AsyncTask<Sensor, Void, RegisteredSensorsMessage> {

    public interface TaskDelegate {
        public void onGetSensorDataById(List<NameValuePair> measured);
    }

    private ArrayList<TaskDelegate> delegateRef;
    private ArrayList<User> userRef;

    public GetSensorDataByIdTask(ArrayList<TaskDelegate> delegateRef, ArrayList<User> userRef) {
        this.delegateRef = delegateRef;
        this.userRef = userRef;
    }

    private String s_uuid;

    @Override
    protected RegisteredSensorsMessage doInBackground(Sensor ... sensors) {

        if(sensors.length == 0) return null;
        try {
            s_uuid = sensors[0].getUuid();
            //ClientResource cr = new ClientResource("http://147.32.107.139:8080/registered_sensors/" + sensors[0].getUuid());
            ClientResource cr = new ClientResource("http://147.32.107.139:8080/registered_sensors");
            Log.e("SensorUUID", sensors[0].getUuid());
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
                    userRef.get(0).getUsername(), userRef.get(0).getPassword());
            Log.e("Account", userRef.get(0).getUsername() + ":" + userRef.get(0).getPassword());
            RegisteredSensors sr = cr.wrap(RegisteredSensors.class);
            return sr.retrieve_2();
            //return sr.get(sensors[0].getUuid());
        } catch (Exception e) {
            Log.e("RegedSensorsTask", e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(RegisteredSensorsMessage message) {

        delegateRef.get(0).onGetSensorDataById(message.getSensorData(s_uuid));
    }
}