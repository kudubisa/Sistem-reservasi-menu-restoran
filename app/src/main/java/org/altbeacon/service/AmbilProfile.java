package org.altbeacon.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.menu.balerasa.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrya on 1/12/2016.
 */
public class AmbilProfile extends AsyncTask<Object,Object,Object>{
    private JSONParser jsonParser;
    JSONObjectResultAP jsonObjectResult;
    ProgressDialog progDialog;
    Context context;
    String url = "http://192.168.42.48/Kuliner/JSON/ambil_profile.php";
    public void init(Context c, JSONObjectResultAP jobres){
        context = c;
        jsonObjectResult = jobres;
        final String PREFS_NAME = "logged";
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String email = settings.getString("email","");
        AmbilProfile ap = this;
        ap.execute(url,email);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = ProgressDialog.show(context, "Mengakses Data", "aaa");
        progDialog.setMessage("Mohon Tungggu...");
        progDialog.show();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        JSONObject jsonObject = null;
        String url = (String) params[0];
        String email = (String) params[1];
        jsonParser = new JSONParser();
        List<NameValuePair> dataJson = new ArrayList<NameValuePair>();
        dataJson.add(new BasicNameValuePair("email",email));
        try {
            jsonObject = jsonParser.getJsonOBject(url,"POST",dataJson);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        Log.e("Test1", "Enter On post");
        progDialog.dismiss();
        if(result!=null) {

            Log.e("Test2","Enter If");
            JSONObject jobj = (JSONObject) result;
            jsonObjectResult.gotJSONObject(jobj);

        }
    }

    public static abstract class JSONObjectResultAP{
        public abstract void gotJSONObject(JSONObject jsonObject);
    }

}
