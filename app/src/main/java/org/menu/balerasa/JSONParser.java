package org.menu.balerasa;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Andrya on 8/18/2015.
 */
public class JSONParser {

    InputStream is;
    private JSONObject jObject;
    String json;

    public JSONObject getJsonOBject(String url, String mehtod, List<NameValuePair> value) throws IOException {

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity( new UrlEncodedFormEntity(value));
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            json = sb.toString();
            jObject = null;
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            jObject = new JSONObject(json);
        }catch(JSONException e){
            e.printStackTrace();
        }

        return jObject;
    }
}
