package org.menu.balerasa;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beaconreference.R;

/**
 * Created by Andrya on 8/30/2015.
 */
public class VoucherActivity extends Activity {
    Button btAmbil,btLewatkan;
    ImageView imgVoucher;
    TextView txtTitle, txtDetail;
    String code,judul,text,email;
    final String PREFS_NAME = "logged";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        Bundle extras = getIntent().getExtras();
        code = extras.getString("code");
        judul = extras.getString("judul");
        text = extras.getString("text");
        Log.d("Iklan",code);

        if(isLogged()){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            email = settings.getString("email","");
        }else {
            email = getEmail();
        }


        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        btAmbil = (Button) findViewById(R.id.btAmbilVoucher);
        btLewatkan = (Button) findViewById(R.id.btLewatkan);
        imgVoucher = (ImageView) findViewById(R.id.imgVoucher);
        imgVoucher.setImageBitmap(bmp);
        btAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilVoucher av = new AmbilVoucher();
                av.init(VoucherActivity.this, email, code, gtm);

            }
        });

        btLewatkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(VoucherActivity.this, MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(judul);
        txtDetail = (TextView) findViewById(R.id.txtDetail);
        txtDetail.setText(text);
    }

    AmbilVoucher.GoToMain gtm = new AmbilVoucher.GoToMain() {
        @Override
        public void goToMain() {
            //Intent intent = new Intent(VoucherActivity.this, MainActivity.class);
            //startActivity(intent);
            finish();
        }
    };

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
