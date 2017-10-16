package org.altbeacon.service;

import android.content.Context;
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
public class AmbilVoucher extends AsyncTask<Object,Object,Object>{
    private JSONParser jsonParser;
    JSONObjectResult jsonObjectResult;
    Context context;
    String url = "http://192.168.42.48/Kuliner/JSON/voucher_service.php";
    public void init(Context c, JSONObjectResult jobres, String minor,String email){
        context = c;
        jsonObjectResult = jobres;
        AmbilVoucher av = this;
        av.execute(minor,url,email);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        JSONObject jsonObject = null;
        String minor = (String) params[0];
        String url = (String) params[1];
        String email = (String) params[2];
        jsonParser = new JSONParser();
        List<NameValuePair> dataJson = new ArrayList<NameValuePair>();
        dataJson.add(new BasicNameValuePair("minor",minor));
        dataJson.add(new BasicNameValuePair("email",email));
        Log.d("eusi minor",minor);
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
        if(result!=null){
            Log.e("Test2","Enter If");
            JSONObject jobj = (JSONObject) result;
            jsonObjectResult.gotJSONObject(jobj);
        }
    }

    public static abstract class JSONObjectResult{
        public abstract void gotJSONObject(JSONObject jsonObject);
    }

}
