package org.menu.balerasa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrya on 8/26/2015.
 */
public class DeleteCart extends AsyncTask<String,String,String> {
    Context context;
    JSONParser jsonParser;
    ProgressDialog progress;
    String url = "http://192.168.42.48/Kuliner/JSON/delete_cart.php";
    String id_menu = "";
    String em = "";


    public void init(Context c,String email,String idMenu){
        this.context = c;
        this.id_menu = idMenu;
        this.em = email;
        DeleteCart deleteCart = this;
        deleteCart.execute();
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
        String idMenu= id_menu;

        List<NameValuePair> value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("id_menu", idMenu));
        value.add(new BasicNameValuePair("email",em));

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
                Log.i("Sukses", "Menu telah dihapus dari keranjang");
            }else{
                Log.d("Error", "Gagal menghapus menu dari keranjang");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        progress.dismiss();
    }

}
