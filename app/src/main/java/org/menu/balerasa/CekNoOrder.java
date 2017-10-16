package org.menu.balerasa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrya on 8/22/2015.
 */
public class CekNoOrder extends AsyncTask<Object,Object,Object> {
    SetNorMej setNorMej;
    JSONParser jsonParser;
    ProgressDialog pd;
    Context context;
    JSONObject jsonObject = null;

    String no_order="";
    String no_meja="";
    String url = "http://192.168.42.48/Kuliner/JSON/cek_no_order.php";
    public void init(Context c, String email, SetNorMej setNorMej){
        this.context = c;
        jsonParser = new JSONParser();
        this.setNorMej = setNorMej;
        CekNoOrder cekNoOrder = this;
        cekNoOrder.execute(email);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = ProgressDialog.show(context, "Cek Status", "aaa");
        pd.setMessage("Mohon Tunggu");
        pd.show();
    }

    @Override
    protected Object doInBackground(Object... params) {
        JSONObject jsonObject = null;
        String email = (String) params[0];
        List<NameValuePair> dataJson = new ArrayList<NameValuePair>();
        dataJson.add(new BasicNameValuePair("email", email));
        try {
            jsonObject = jsonParser.getJsonOBject(url,"POST",dataJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsonObject != null) {
            //Log.e("Galat", "Teu kapanggih");
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("tmp_order");
                for (int i = 0; i < jsonArray.length(); i++) {
                    no_order = jsonArray.getJSONObject(i).getString("no_order");
                    no_meja = jsonArray.getJSONObject(i).getString("no_meja");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        pd.dismiss();
        //Toast.makeText(context, no_order+" "+no_meja, Toast.LENGTH_LONG).show();
        if(no_order != ""){
            setNorMej.setOrMej(no_order, no_meja);
            Toast.makeText(context,"No order aya ceng",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"no_order di cek order kosong mang",Toast.LENGTH_LONG).show();
            setNorMej.setOrMej("kosong", "kosong");
        }
    }



    public static abstract class SetNorMej{
        public abstract void setOrMej(String noOrder, String noMeja);
    }

}
