package org.altbeacon.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beaconreference.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.menu.balerasa.ExitActivity;
import org.menu.balerasa.IklanActivity;
import org.menu.balerasa.MainActivity;
import org.menu.balerasa.QRCodeActivity;
import org.menu.balerasa.VoucherActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created by Andrya on 9/3/2015.
 */
public class BeaconService extends Service implements BeaconConsumer {
    String urlp = "http://192.168.42.48/Kuliner/";
    private BeaconManager beaconManager;
    private boolean haveSeenBeacon = false;
    private String uuID = "e20a39f4-73f5-4bc4-a12f-17d1ad07a961";
    int minorID=0;
    int min=0;
    EntitasVoucher entitasVoucher;
    ArrayList<EntitasVoucher> ev = new ArrayList<EntitasVoucher>();
    Bitmap bitmap = null;
    final String PREFS_NAME = "logged";
    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundMode(true);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.setBackgroundScanPeriod(1000);
        beaconManager.setBackgroundBetweenScanPeriod(1000);
        beaconManager.bind(this);

        Log.d("Naon","Recreating");

    }

    public boolean enterResto(Context context){
        if(haveSeenBeacon==true){
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        Log.d("Naon","Destroyed");
    }

    //Get Primary Email from Device
    public String getEmail(){
        String primaryEmail="";

        try{
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

            for (Account account : accounts) {
                primaryEmail = account.name;
            }
        }
        catch(Exception e)
        {
            Log.i("Exception", "Exception:" + e) ;
        }

        Log.i("Exception", "mails:" + primaryEmail) ;

        return primaryEmail;
    }

    public boolean isLogged(){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Reading from SharedPreferences
        int value = settings.getInt("isLoggedIn", 0);
        String email = settings.getString("email","");
        Log.d("email", email);
        if(value == 1) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                String uu = "", major = "", minor = "",email="";

                if(isLogged()){
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    email = settings.getString("email","");
                }else {
                    email = getEmail();
                }

                for (Beacon oneBeacon : beacons) {
                    uu = oneBeacon.getId1().toString();
                    major = oneBeacon.getId2().toString();
                    minor = oneBeacon.getId3().toString();
                    min = oneBeacon.getId3().toInt();


                    Log.d("BeaconsEverywhere", "Data Field : " + oneBeacon.getBluetoothName() +
                            "distance : " + oneBeacon.getDistance() + ", UUID : " + uu +
                            ", Major ID : " + major + ", Minor ID : " + minor);


                }

                if (minorID != min) {

                    minorID = min;
                    Log.d("Alpha","minorId = "+minor+" dan min = "+min);
                    ev.clear();
                    AmbilVoucher ambilVoucher = new AmbilVoucher();
                    ambilVoucher.init(BeaconService.this, jsonObjectResult, String.valueOf(minorID), email);
                }


            }


        });

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d("Beacon", "Beacon Kapanggih...");

                if (haveSeenBeacon == false) {
                    haveSeenBeacon = true;
                }

                try {
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


                //To determine wether on site or not
                final String PREFS_NAME = "OnSite";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("isOnSite", 1);
                editor.commit();

            }

            @Override
            public void didExitRegion(Region region) {
                Log.d("Beacon", "Kaluar tina area Beacon...");
                showNotificationOutRegion();
                try {
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                //To determine wether on site or not
                final String PREFS_NAME = "OnSite";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("isOnSite", 0);
                editor.commit();

            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });



        //Identifier identifier = Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0");
        Identifier identifier = Identifier.parse(uuID);
        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("Beacon 1",null,null,null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }



    }


    AmbilVoucher.JSONObjectResult jsonObjectResult = new AmbilVoucher.JSONObjectResult() {
        @Override
        public void gotJSONObject(JSONObject jsonObject) {
            try {
                JSONArray arrayMenu = jsonObject.getJSONArray("datavoucher");
                for (int i = 0; i < arrayMenu.length(); i++) {
                    entitasVoucher = new EntitasVoucher();
                    entitasVoucher.setTitle(arrayMenu.getJSONObject(i)
                            .getString("judul"));
                    entitasVoucher.setText(arrayMenu.getJSONObject(i)
                            .getString("deskripsi"));
                    entitasVoucher.setPic(arrayMenu.getJSONObject(i)
                            .getString("image"));
                    entitasVoucher.setMinId(arrayMenu.getJSONObject(i)
                            .getInt("minor_id"));
                    entitasVoucher.setCode(arrayMenu.getJSONObject(i)
                            .getString("code"));
                    ev.add(entitasVoucher);

                }


                Random rd = new Random();
                final int rdm = rd.nextInt(arrayMenu.length());
                Log.d("random","masuk ambil voucher dan random="+Integer.toString(rdm));
                new Thread(new GetBitmapTask(urlp+ev.get(rdm).getPic(), new GetBitmapTask.Callback() {
                    @Override public void onFinish(Bitmap bm) {
                        //panggil notifikasi selain sayonara
                        showNotification(ev.get(rdm).getCode(),ev.get(rdm).getTitle(), ev.get(rdm).getText(), ev.get(rdm).getMinId(),bm);
                    }

                    @Override public void onError(Throwable t) {

                    }
                })).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //notif lama
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Restoran Bale Rasa")
                        .setContentText("Selamat Datang Di Restoran Bale Rasa.")
                        .setSmallIcon(R.drawable.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, VoucherActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent( 0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public static class GetBitmapTask implements Runnable {

        private final String uri;
        private final Callback callback;

        public GetBitmapTask(String uri, Callback callback) {
            this.uri = uri;
            this.callback = callback;
        }

        @Override public void run() {
            try {
                URL url = new URL(uri);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                callback.onFinish(bmp);
            } catch (IOException e) {
                callback.onError(e);
            }
        }

        public interface Callback{
            void onFinish(Bitmap bitmap);
            void onError(Throwable t);
        }
    }

    public void showNotification(String code,String judul, String text, int min_id, Bitmap bitmap) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.voucher);
        Notification.BigPictureStyle bigPic = new Notification.BigPictureStyle().bigPicture(bitmap);
        bigPic.setSummaryText(text);
        Intent intent;
        String title = judul;;
        // intent triggered, you can add other intent for other actions
        Log.d("MinId", Integer.toString(min_id));
        if (min_id == 1) {
            title = "Selamat Datang Di Bale Roso!";
            Log.d("MinId", "minor 1");
            intent = new Intent(BeaconService.this, VoucherActivity.class);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("code", code);
            intent.putExtra("judul", judul);
            intent.putExtra("text", text);
            intent.putExtra("bitmap",byteArray);
            intent.setAction("actionstring" + System.currentTimeMillis());


        }else if (min_id == 2) {
            Log.d("MinId", "minor 2");

            intent = new Intent(BeaconService.this, IklanActivity.class);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("code", code);
            intent.putExtra("judul", judul);
            intent.putExtra("text", text);
            intent.putExtra("bitmap",byteArray);
            intent.setAction("actionstring" + System.currentTimeMillis());

        }else if(min_id == 3){
            //table Area
            Log.d("MinId", "minor 3");
            intent = new Intent(BeaconService.this, MainActivity.class);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("code", code);
            intent.putExtra("bitmap",byteArray);
            intent.setAction("actionstring" + System.currentTimeMillis());
        }else{
            //table Casir
            Log.d("MinId", "minor 4");
            intent = new Intent(BeaconService.this, QRCodeActivity.class);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("code", code);
            intent.putExtra("bitmap",byteArray);
            intent.setAction("actionstring" + System.currentTimeMillis());
        }

        PendingIntent pIntent = PendingIntent.getActivity(BeaconService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(bigPic)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .setPriority(Notification.PRIORITY_MAX)
                //.addAction(0, "View", pIntent)
                //.addAction(0, "Tutup", pIntent)

                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }



    //notif lama
    private void sendNotifExit() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Restoran Bale Rasa")
                        .setContentText("Selamat Jalan, Sampai berkunjung kembali")
                        .setSmallIcon(R.drawable.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, ExitActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }


    public void showNotificationOutRegion(){

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(BeaconService.this, 0,intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle("Restoran Bale Rasa")
                .setContentText("Selamat Jalan Sampai Berjumpa Lagi.")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(soundUri)

                //.addAction(0, "View", pIntent)
                //.addAction(0, "Remind", pIntent)

                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
