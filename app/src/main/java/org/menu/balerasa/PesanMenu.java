package org.menu.balerasa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
 * Created by Andrya on 8/19/2015.
 */
public class PesanMenu extends AsyncTask<String,String,String> {
    Context context;
    JSONParser jsonParser;
    ProgressDialog progress;
    String url = "http://192.168.42.48/Kuliner/JSON/order.php";
    String noOrder = "";
    String jml = "";
    String id_menu = "";
    String em = "";
    String noMeja = "";

    SetOrderNum setOrderNum;

    public void init(Context c,String email,String idMenu,String noOrder, String jml, SetOrderNum setOrderNum){
        this.context = c;
        this.setOrderNum = setOrderNum;
        this.id_menu = idMenu;
        this.noOrder = noOrder;
        this.jml = jml;
        this.em = email;
        //Toast.makeText(context, idMenu + " " + noOrder + " " + jml + " " + em, Toast.LENGTH_LONG).show();
        final String PREFS2_NAME = "Pemesanan";
        SharedPreferences setMeja = context.getSharedPreferences(PREFS2_NAME, Context.MODE_PRIVATE);
        noMeja=setMeja.getString("no_meja", "");
        PesanMenu pesanMenu = this;
        pesanMenu.execute();
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
        String qty = jml;

        String no_order = noOrder;

        String result = null;

        List<NameValuePair> value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("id_menu", idMenu));
        value.add(new BasicNameValuePair("qty", qty));
        value.add(new BasicNameValuePair("no_meja", noMeja));
        value.add(new BasicNameValuePair("no_order",no_order));
        value.add(new BasicNameValuePair("email",em));
        Log.d("id_menu", idMenu);
        Log.d("qty", qty);
        Log.d("no_meja", noMeja);
        Log.d("no_order",no_order);
        Log.d("cek email",em);
        JSONObject js = null;
        try {
            js = jsonParser.getJsonOBject(url, "POST", value);
            Log.d("json error", "error disini 1");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Log.d("json error", "error disini 2");
        }

        try {
            int status = js.getInt("status");
            if(status == 1){
                Log.i("Sukses", "Produk telah ditambah");
                result = js.getString("no_order");
            }else{
                Log.d("Error", "Gagal menambah produk");
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
        if(result!=null) {
            setOrderNum.setNumb(result);
            Toast.makeText(context,"Menu berhasil ditambahkan!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Menu gagal ditambahkan!",Toast.LENGTH_SHORT).show();
        }
        progress.dismiss();
    }

    public static abstract class SetOrderNum{
        public abstract void setNumb(String result);
    }

}