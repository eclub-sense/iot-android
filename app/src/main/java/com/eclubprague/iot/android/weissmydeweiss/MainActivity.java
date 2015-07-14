package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.SensorRegistrator;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorListViewAdapter;

import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.resource.ClientResource;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        List<Sensor> sensorList = new ArrayList<>();
        sensorList.add(new Sensor(123, SensorType.THERMOMETER, 12345));
        sensorList.add(new Sensor(34345, SensorType.LED, 8878));
        sensorList.add(new Sensor(3677, SensorType.THERMOMETER, 33442));

        ListView sensors = (ListView) findViewById(R.id.sensorsListView);
        sensors.setAdapter(new SensorListViewAdapter(this, R.layout.item_img_twolines, sensorList));

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public void naskenujKod(View view) {
        Intent myIntent = new Intent(MainActivity.this, CameraActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivityForResult(myIntent, CameraActivity.REQUEST_SCAN_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == CameraActivity.REQUEST_SCAN_QR_CODE) {
                if(resultCode == RESULT_OK) {
                    try {
                        final String qrcode = data.getStringExtra(CameraActivity.RESULT_BARCODE);
                        // send the code to the audience
                        final int numero = Integer.parseInt(qrcode.split(";")[0]);

                        Thread thr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Engine.getInstance().getRegisteredConverters().add(new GsonConverter());

                                // try connection
                                ClientResource cr = new ClientResource("http://192.168.201.222:8080/sensor_registration");
                                SensorRegistrator sr = cr.wrap(SensorRegistrator.class);

                                Sensor sensor = new Sensor(numero, SensorType.THERMOMETER, 12345);
                                sr.store(sensor);
                            }
                        });
                        thr.start();

                    } catch(NumberFormatException e) {
                        Toast t2 = Toast.makeText(this, "Exception: NaN", Toast.LENGTH_SHORT);
                        t2.show();
                    }
                }
            }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
