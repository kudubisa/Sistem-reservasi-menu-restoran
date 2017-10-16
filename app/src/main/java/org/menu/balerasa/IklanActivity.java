package org.menu.balerasa;

import android.app.Activity;
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
 * Created by Andrya on 1/15/2016.
 */
public class IklanActivity extends Activity {
    Button btTutup;
    ImageView imgVoucher;
    TextView txtJudul,txtDetail;
    String code,judul,text,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklan);
        Bundle extras = getIntent().getExtras();
        code = extras.getString("code");
        judul = extras.getString("judul");
        text = extras.getString("text");
        Log.d("Iklan", code);

        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imgVoucher = (ImageView) findViewById(R.id.imgVoucher);
        imgVoucher.setImageBitmap(bmp);

        txtJudul = (TextView) findViewById(R.id.txtTitle);
        txtJudul.setText(judul);
        txtDetail = (TextView) findViewById(R.id.txtDetail);
        txtDetail.setText(text);

        btTutup = (Button) findViewById(R.id.btTutup);
        btTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(IklanActivity.this, MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });

    }


}
