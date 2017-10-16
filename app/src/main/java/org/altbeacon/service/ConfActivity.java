package org.altbeacon.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.altbeacon.beaconreference.R;

/**
 * Created by Andrya on 9/3/2015.
 */

public class ConfActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);
        Button btnStartService = (Button) findViewById(R.id.btn_start_service);
        Button btnStopService = (Button) findViewById(R.id.btn_stop_service);
        Button btnEnterResto = (Button) findViewById(R.id.btn_enter_resto);


        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(ConfActivity.this, BeaconService.class));
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(ConfActivity.this, BeaconService.class));
            }
        });

        btnEnterResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconService bs = new BeaconService();
                boolean res = bs.enterResto(ConfActivity.this);
                if(res==true){
                    finish();
                }
            }
        });



    }

}