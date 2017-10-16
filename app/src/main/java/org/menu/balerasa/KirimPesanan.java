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
 * Created by Andrya on 8/27/2015.
 */
public class KirimPesanan extends AsyncTask<String, String, String> {
    Context context;
    JSONParser jsonParser;
    ProgressDialog progress;
    String url = "http://192.168.42.48/Kuliner/JSON/kirim_pesanan.php";
    String em = "";
    String cv = "";


    public void init(Context c,String email, String codeVoucher){
        this.context = c;
        this.em = email;
        this.cv = codeVoucher;
        KirimPesanan kirimPesanan = this;
        kirimPesanan.execute();
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
                Log.i("Sukses", "Pesanan telah dikirim ke dapur");
                result = "Berhasil";
            }else{
                Log.d("Error", "Gagal mengirim pesanan ke dapur");
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
        if(result != null){
            Toast.makeText(context,"Pesanan anda berhasil dikirim!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"Pesanan anda gagal dikirim!",Toast.LENGTH_LONG).show();
        }
    }

}
