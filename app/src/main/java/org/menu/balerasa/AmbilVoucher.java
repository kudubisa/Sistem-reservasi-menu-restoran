package org.menu.balerasa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrya on 1/14/2016.
 */
public class AmbilVoucher extends AsyncTask<String,String,String>{

    Context context;
    GoToMain goToMain;
    JSONParser jsonParser;
    ProgressDialog progress;
    String url = "http://192.168.42.48/Kuliner/JSON/ambil_voucher.php";
    String em = "";
    String cv = "";


    public void init(Context c,String email, String codeVoucher, GoToMain gtm){
        this.context = c;
        this.goToMain = gtm;
        this.em = email;
        this.cv = codeVoucher;
        AmbilVoucher av = this;
        av.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        jsonParser = new JSONParser();
        progress = new ProgressDialog(context);
        progress.setMax(100);
        progress.setIndeterminate(false);
        progress.show();
    }
    @Override
    protected String doInBackground(String... params) {
        String result = null;
        List<NameValuePair> value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("email",em));
        value.add(new BasicNameValuePair("codeVoucher",cv));

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
                Log.i("Sukses", "Voucher berhasil didapatkan");
                result = "Berhasil";
            }else{
                Log.d("Error", "Voucher gagal didapatkan");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progress.dismiss();
        if(result != null){
            Toast.makeText(context, "Anda Mendapatkan Voucher! Silahkan Gunakan pada saat melakukan pesanan", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"Voucher tidak tersedia atau anda sudah memilik voucher!",Toast.LENGTH_LONG).show();
        }
        goToMain.goToMain();
    }

    public static abstract class GoToMain{
        public abstract void goToMain();
    }

}
