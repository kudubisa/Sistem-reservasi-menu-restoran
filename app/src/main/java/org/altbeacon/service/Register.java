package org.altbeacon.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
public class Register extends AsyncTask<String, String, String> {
    Context context;
    JSONParser jsonParser;
    ProgressDialog progress;
    Success success;
    String url = "http://192.168.42.48/Kuliner/JSON/register.php";
    String em = "";
    String pass = "";
    String passbar = "";
    String nm = "";
    String almt = "";
    String ntlp = "";


    public void init(Context c,String email, String password, String pb, String nama, String alamat, String no_telp, Success scs){
        this.context = c;
        success = scs;
        this.em = email;
        this.pass = password;
        this.passbar = pb;
        this.nm = nama;
        this.almt = alamat;
        this.ntlp = no_telp;
        Register rg = this;
        rg.execute();
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
        List<NameValuePair> value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("email",em));
        value.add(new BasicNameValuePair("password",pass));
        value.add(new BasicNameValuePair("passwordBaru",passbar));
        value.add(new BasicNameValuePair("nama",nm));
        value.add(new BasicNameValuePair("alamat",almt));
        value.add(new BasicNameValuePair("no_telp",ntlp));

        JSONObject js = null;
        try {
            js = jsonParser.getJsonOBject(url, "POST", value);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            int status = js.getInt("status");
            if(status == 1){
                Log.i("Sukses", "Pendaftaran Berhasil");
                result = "Profile Anda Berhasil Di Simpan!";
            }else if(status == 0){
                result = null;
                Log.d("Error", "Pendaftaran Gagal");
            }else if(status == 2){
                result = "Password yang anda masukan salah!";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        progress.dismiss();
        if(result == null){
            Toast.makeText(context,"Profile Anda Gagal Di Simpan!",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            success.refresh();
        }
    }

    public static abstract class Success{
        public abstract void refresh();
    }
}