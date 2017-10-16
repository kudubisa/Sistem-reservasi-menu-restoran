package org.menu.balerasa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrya on 8/18/2015.
 */
public class AmbilData extends AsyncTask<Object,Object,Object> {

    ProgressDialog progDialog;
    private JSONParser jsonParser;
    Context context;
    JsonObjectResult jobRes;

    public void init(Context c,JsonObjectResult jres, String kategori,String url ){
        this.context = c;
        this.jobRes = jres;
        AmbilData ad = this;
        ad.execute(kategori,url);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = ProgressDialog.show(context, "Mengakses Data","aaa");
        progDialog.setMessage("Mohon Tungggu...");
        progDialog.show();
    }

    @Override
    protected Object doInBackground(Object... params) {

        JSONObject jsonObject = null;
        String url = (String) params[1];
        String kat = (String) params[0];
        jsonParser = new JSONParser();
        List<NameValuePair> dataJson = new ArrayList<NameValuePair>();
        dataJson.add(new BasicNameValuePair("kategori",kat));
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
        if(progDialog.isShowing()){
            progDialog.dismiss();
        }
        Log.e("Test4","Enter On post");
        if(result!=null){
            Log.e("Test3","Enter If");
            JSONObject jobj = (JSONObject) result;
            jobRes.gotJsonObject(jobj);
        }
    }

    public static abstract class JsonObjectResult{
        public abstract void gotJsonObject(JSONObject object);
    }
}