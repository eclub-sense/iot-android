package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

/**
 * Created by Dat on 16.7.2015.
 */
public class SensorInfoDialog extends AlertDialog.Builder {

    private Context context;
    private Sensor sensor;
    private LinearLayout layout;

    public SensorInfoDialog(Context context, Sensor sensor) {
        super(context);
        this.context = context;
        this.sensor = sensor;

        layout = new LinearLayout(this.context);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView text1=new TextView(this.context);
        text1.setText("Info_1");
        text1.setPadding(5, 5, 5, 5);

        TextView text2=new TextView(this.context);
        text2.setText("Info_2");
        text2.setPadding(5, 5, 5, 5);

        layout.addView(text1);
        layout.addView(text2);

        this.setView(layout);

        this.setTitle(sensor.getType().getName());
        this.setMessage(sensor.toString());

        this.setPositiveButton("Close",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                dialog.dismiss();
            }
        });

        this.setNegativeButton("Unregister", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(SensorInfoDialog.this.context, "Sensor unregistered", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        this.create();
        this.show();
    }


}
