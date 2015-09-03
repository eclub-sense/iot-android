package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorDataDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorInfoDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorsExpandableListViewAdapter;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.MagneticFieldChart;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.OneValueChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.SensorDataChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.ThermometerChartActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dat on 14.7.2015.
 */
public class SensorsListFragment extends Fragment {

    static ExpandableListView expListView;
    SensorsExpandableListViewAdapter adapter;
    List<Hub> groupItems;
    HashMap<Hub, List<Sensor>> childItems;
    View rootView;
    ArrayList<MainActivity> activityRef;

    public static SensorsListFragment newInstance() {
        SensorsListFragment fragment = new SensorsListFragment();

        return fragment;
    }

    public void setMainActivityRef(ArrayList<MainActivity> activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sensors_list, container, false);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.sensors_expList);

        // preparing list data
        prepareListData();

        adapter = new SensorsExpandableListViewAdapter(rootView.getContext(), groupItems, childItems);

        // setting list adapter
        expListView.setAdapter(adapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Sensor sensor = (Sensor) parent.getExpandableListAdapter().
                        getChild(groupPosition, childPosition);
                String token = ((MainActivity) SensorsListFragment.this.getActivity()).getToken().getAccess_token();

                Intent intent = new Intent(SensorsListFragment.this.getActivity(), SensorDataChartActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("uuid", sensor.getUuid());
                intent.putExtra("title", SensorType.getStringSensorType(sensor.getType()));

                switch (sensor.getType()) {

                    case SensorType.GPS:
                        String[] datasetDesc1 = {"longitude", "latitude"};
                        intent.putExtra("datasetDesc", datasetDesc1);
                        intent.putExtra("upperBound", 180f);
                        break;
                    case SensorType.THERMOMETER:
                        Intent intent2 = new Intent(SensorsListFragment.this.getActivity(), ThermometerChartActivity.class);
                        intent2.putExtra("token", token);
                        intent2.putExtra("uuid", sensor.getUuid());
                        startActivity(intent2);
                        //String[] datasetDesc2 = {"increment", "pressure", "temperature", "vbat", "rssi"};
                        //intent.putExtra("datasetDesc", datasetDesc2);
                        //intent.putExtra("upperBound", 45f);
                        break;
                    case SensorType.ACCELEROMETER:
                        String[] datasetDesc3 = {"x", "y", "z"};
                        intent.putExtra("datasetDesc", datasetDesc3);
                        intent.putExtra("upperBound", 15f);
                        break;
                    case SensorType.LIGHT:
                        String[] datasetDesc4 = {"illumination"};
                        intent.putExtra("datasetDesc", datasetDesc4);
                        intent.putExtra("upperBound", 400f);
                        break;
                    case SensorType.PROXIMITY:
                        String[] datasetDesc5 = {"proximity"};
                        intent.putExtra("datasetDesc", datasetDesc5);
                        intent.putExtra("upperBound", 10f);
                        break;
                    case SensorType.MAGNETOMETER:
                        String[] datasetDesc6 = {"x", "y", "z"};
                        intent.putExtra("datasetDesc", datasetDesc6);
                        intent.putExtra("upperBound", 300f);
                        break;
                    case SensorType.GYROSCOPE:
                        String[] datasetDesc7 = {"x", "y", "z"};
                        intent.putExtra("datasetDesc", datasetDesc7);
                        intent.putExtra("upperBound", 10f);
                        break;
                    case SensorType.PRESSURE:
                        String[] datasetDesc8 = {"pressure"};
                        intent.putExtra("datasetDesc", datasetDesc8);
                        intent.putExtra("upperBound", 1100f);
                        break;
                    case SensorType.GRAVITY:
                        String[] datasetDesc9 = {"x", "y", "z"};
                        intent.putExtra("datasetDesc", datasetDesc9);
                        intent.putExtra("upperBound", 300f);
                        break;
                    case SensorType.LINEAR_ACCELEROMETER:
                        String[] datasetDesc10 = {"x", "y", "z"};
                        intent.putExtra("datasetDesc", datasetDesc10);
                        intent.putExtra("upperBound", 15f);
                        break;
                    case SensorType.ROTATION:
                        String[] datasetDesc11 = {"x", "y", "z"};
                        intent.putExtra("datasetDesc", datasetDesc11);
                        intent.putExtra("upperBound", 15f);
                        break;
                    case SensorType.HUMIDITY:
                        String[] datasetDesc12 = {"humidity"};
                        intent.putExtra("datasetDesc", datasetDesc12);
                        intent.putExtra("upperBound", 110f);
                        break;
                    case SensorType.AMBIENT_THERMOMETER:
                        String[] datasetDesc13 = {"temperature"};
                        intent.putExtra("datasetDesc", datasetDesc13);
                        intent.putExtra("upperBound", 45f);
                        break;

                }

                //startActivity(intent);

                getActivity()
                        .overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    private void prepareListData() {
        groupItems = new ArrayList<Hub>();
        childItems = new HashMap<Hub, List<Sensor>>();
    }

}
