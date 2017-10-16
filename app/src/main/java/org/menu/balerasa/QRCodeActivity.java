package org.menu.balerasa;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.altbeacon.beaconreference.R;
import org.lib.zxing.qrcode.QRCodeEncoder;

/**
 * Created by Andrya on 2/29/2016.
 */
public class QRCodeActivity extends Activity {

    final String PREFS_NAME = "logged";
    String email="";
    Button btTutup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_qrcode);

        if(isLogged()){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            email = settings.getString("email","");
        }else {
            email = getEmail();
        }

        //QRCodeCaller
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        showQRCode(email, smallerDimension);

        btTutup = (Button) findViewById(R.id.btTutup);
        btTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showQRCode(String text, int dmn){


        ImageView myImage = (ImageView) findViewById(R.id.imageView1);

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(text,
                null,
                com.google.zxing.client.android.Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                dmn);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();

            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

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

}
