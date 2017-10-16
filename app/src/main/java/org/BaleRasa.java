package org;

import android.app.Application;
import android.util.Log;

/**
 * Created by Andrya on 9/3/2015.
 */
public class BaleRasa extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //startService(new Intent(this, BeaconService.class));
        Log.d("Voila", "ini otomatis akan dibuat ketika aplikasi dijalankan");
    }
}
