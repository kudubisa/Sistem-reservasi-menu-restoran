package org.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.altbeacon.service.BeaconService;

/**
 * Created by Andrya on 9/3/2015.
 */
public class BluetoothModeChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
            switch(state){
                case BluetoothAdapter.STATE_ON:
                    //Toast.makeText(context,"Bluetooth is On",Toast.LENGTH_LONG);
                    Log.d("Tobir","Bluetooth is On");
                    context.startService(new Intent(context, BeaconService.class));
                    break;
                case BluetoothAdapter.STATE_OFF:
                    //Toast.makeText(context,"Bluetooth is Off",Toast.LENGTH_LONG);
                    Log.d("Tobir","Bluetooth is Off");
                    context.stopService(new Intent(context, BeaconService.class));
                    break;
            }
        }
    }
}

