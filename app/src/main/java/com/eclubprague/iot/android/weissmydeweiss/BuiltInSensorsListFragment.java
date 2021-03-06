package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.BuiltInSensorInfoDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.BuiltInSensorsListViewAdapter;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.AccelerometerChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.GyroscopeChart;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.LightSensorChart;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.MagneticFieldChart;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.ProximityChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 20.7.2015.
 */
public class BuiltInSensorsListFragment extends Fragment {

    static ListView listView;
    private SensorManager mSensorManager;
    private Context context;

    public static BuiltInSensorsListFragment newInstance() {
        BuiltInSensorsListFragment fragment = new BuiltInSensorsListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_hubs_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.hubs_list);

        // Defined Array values to show in ListView
        mSensorManager = (SensorManager) this.getActivity().getSystemService(this.getActivity().SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(	android.hardware.Sensor.TYPE_ALL);

//        List<String>values = new ArrayList<>();
//        for(int i = 0; i < deviceSensors.size(); i++) {
//            values.add(deviceSensors.get(i).getName());
//        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        BuiltInSensorsListViewAdapter adapter = new BuiltInSensorsListViewAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, deviceSensors);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //new BuiltInSensorInfoDialog(rootView.getContext(), (Sensor) parent.getAdapter().getItem(position));
                Sensor sensor = (Sensor) parent.getAdapter().getItem(position);
                Intent intent;
                switch (sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), AccelerometerChartActivity.class);
                        break;
                    case Sensor.TYPE_LINEAR_ACCELERATION:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), AccelerometerChartActivity.class);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), MagneticFieldChart.class);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), MagneticFieldChart.class);
                        break;
                    case Sensor.TYPE_PROXIMITY:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), ProximityChart.class);
                        break;
                    case Sensor.TYPE_LIGHT:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), LightSensorChart.class);
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), GyroscopeChart.class);
                        break;
                    case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), GyroscopeChart.class);
                        break;
                    case Sensor.TYPE_ROTATION_VECTOR:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), GyroscopeChart.class);
                        break;
                    case Sensor.TYPE_ORIENTATION:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), GyroscopeChart.class);
                        break;
                    case Sensor.TYPE_GRAVITY:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), GyroscopeChart.class);
                        break;
                    case Sensor.TYPE_GAME_ROTATION_VECTOR:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), GyroscopeChart.class);
                        break;
                    default:
                        intent = new Intent(BuiltInSensorsListFragment.this.getActivity(), SensorChartActivity.class);
                }
                intent.putExtra("sensorName", ((Sensor) parent.getAdapter().getItem(position)).getName());
                startActivity(intent);

                BuiltInSensorsListFragment.this.getActivity()
                        .overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
            }
        });

        context = rootView.getContext();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }
}

