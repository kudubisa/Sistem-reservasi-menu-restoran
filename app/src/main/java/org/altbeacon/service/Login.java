package org.altbeacon.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.menu.balerasa.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrya on 1/22/2016.
 */
public class Login extends AsyncTask<String, String, String> {
    Context context;
    Success scs;
    JSONParser jsonParser;
    ProgressDialog progress;
    String url = "http://192.168.42.48/Kuliner/JSON/login_check.php";
    String em = "";
    String pass = "";


    public void init(Context c,String email, String password, Success success){
        this.scs = success;
        this.context = c;
        this.em = email;
        this.pass = password;
        Login lg = this;
        lg.execute();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        jsonParser = new JSONParser();
        progress = new ProgressDialog(context);
        progress.setMax(100);
        progress.setIndeterminate(false);
        progress.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        String result = null;
        int hasil=0;
        List<NameValuePair> value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("email",em));
        value.add(new BasicNameValuePair("password",pass));

        JSONObject js = null;
        try {
            js = jsonParser.getJsonOBject(url, "POST", value);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            hasil = js.getInt("anjing");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(hasil==1){
            result="Berhasil";
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        progress.dismiss();
        if(result != null){
            Toast.makeText(context, "Anda Berhasil Login!", Toast.LENGTH_LONG).show();
            final String PREFS_NAME = "logged";
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            // Writing data to SharedPreferences
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("isLoggedIn", 1);
            editor.putString("email", em);
            editor.commit();

            // Reading from SharedPreferences
            //String value = settings.getString("isLoggedIn", "");
            //Log.d("isLoggedIn", value);

            Intent myIntent = new Intent(context, KonfigurasiPenggunaActivity.class);
            //myIntent.putExtra("RecentProblemId", result);
            context.startActivity(myIntent);
            scs.loginStat(result);


        }else{
            Toast.makeText(context,"Login Gagal!",Toast.LENGTH_LONG).show();
        }
    }

    public static abstract  class Success{
        public abstract void loginStat(String result);
    }

}
