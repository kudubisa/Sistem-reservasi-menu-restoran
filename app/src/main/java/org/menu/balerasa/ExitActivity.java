package org.menu.balerasa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.altbeacon.beaconreference.R;

/**
 * Created by Andrya on 8/30/2015.
 */
public class ExitActivity extends Activity{
    Button btSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        btSkip = (Button) findViewById(R.id.btClose);

        btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
