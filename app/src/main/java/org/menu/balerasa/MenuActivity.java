package org.menu.balerasa;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.altbeacon.beaconreference.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.menu.balerasa.AmbilData.JsonObjectResult;
import org.menu.balerasa.PesanMenu.SetOrderNum;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrya on 8/18/2015.
 */
public class MenuActivity extends Activity {

    EntitasMakanan entitasMakanan;
    ArrayList<EntitasMakanan> menu = new ArrayList<EntitasMakanan>();
    ListView lv;
    String url = "http://192.168.42.48/Kuliner/JSON/menu_services.php";
    String urlpic = "http://192.168.42.48/Kuliner/";
    String no_order="";
    String email = "";
    EditText edOrderNum;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listview);
        lv = (ListView) findViewById(R.id.listItem);
        edOrderNum = (EditText) findViewById(R.id.edOrderNum);
        Bundle b = this.getIntent().getExtras();
        if (b.containsKey("kategori")) {
            String kat = b.getString("kategori");
            email = b.getString("email");
            new CekAtuh().execute();
            AmbilData ambilData = new AmbilData();
            ambilData.init(MenuActivity.this, jsresult, kat, url);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Intent in = new Intent(MenuActivity.this, MainActivity.class);
        //startActivity(in);


    }

    public SetOrderNum setOrderNum = new SetOrderNum() {
        @Override
        public void setNumb(String result) {
            edOrderNum.setText(result);
        }
    };

    public JsonObjectResult jsresult = new JsonObjectResult() {
        @Override
        public void gotJsonObject(JSONObject object) {

            try {
                JSONArray arrayMenu = object.getJSONArray("datamenu");
                for (int i = 0; i < arrayMenu.length(); i++) {
                    entitasMakanan = new EntitasMakanan();
                    entitasMakanan.setIDmenu(arrayMenu.getJSONObject(i)
                            .getString("id"));
                    entitasMakanan.setNamaMenu(arrayMenu.getJSONObject(i)
                            .getString("nama"));
                    entitasMakanan.setHargaMenu(arrayMenu.getJSONObject(i)
                            .getString("harga"));
                    entitasMakanan.setMobDeal(arrayMenu.getJSONObject(i)
                            .getString("harga_mob"));
                    entitasMakanan.setStatus(arrayMenu.getJSONObject(i)
                            .getString("status"));
                    entitasMakanan.setDeskripsiMenu(arrayMenu
                            .getJSONObject(i).getString("deskripsi"));
                    entitasMakanan.setPicMenu(arrayMenu.getJSONObject(i)
                            .getString("image"));
                    menu.add(entitasMakanan);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MenuBaseAdapter menuBaseAdapter = new MenuBaseAdapter(MenuActivity.this, menu);
            lv.setAdapter(menuBaseAdapter);
            lv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String idMenu = menu.get(position).getIDmenu();
                    String nm = menu.get(position).getNamaMenu();
                    String hm = menu.get(position).getHargaMenu();
                    String st = menu.get(position).getStatus();
                    String dm = menu.get(position).getDeskripsimenu();
                    String pic = menu.get(position).getPicMenu();

                    tampilkanDetail(idMenu, nm, hm, st, dm, pic);
                }
            });

        }
    };


    //method tampilkan detail
    public void tampilkanDetail(final String idMenu, String nama, String hrg, String stat, String desk, String pic) {
        final Dialog d = new Dialog(this);
        d.setTitle("Detail Menu");
        d.setContentView(R.layout.dialog_custom);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;

        d.getWindow().setAttributes(lp);

        ImageView image = (ImageView) d.findViewById(R.id.picDialog);
        TextView nm = (TextView) d.findViewById(R.id.idNamaMenu);
        TextView hg = (TextView) d.findViewById(R.id.idHarga);
        TextView st = (TextView) d.findViewById(R.id.idStatus);
        TextView des = (TextView) d.findViewById(R.id.idDeskripsi);
        Button btKembl = (Button) d.findViewById(R.id.idOK);
        Button btPsn = (Button) d.findViewById(R.id.idPesan);
        nm.setText(nama);
        hg.setText(hrg);
        st.setText(stat);
        des.setText(desk);
        new DownloadImageTask(image).execute(urlpic + pic);
        btKembl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        //final String harga = hrg;

        btPsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                new CekAtuh().execute();
                pesanMenu(idMenu);
            }
        });
        d.show();
    }


    //method Pesan
    public void pesanMenu(final String idMenu) {
        final Dialog dlg = new Dialog(this);
        dlg.setTitle("Jumlah Menu");
        dlg.setContentView(R.layout.dialog_pesanan);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.width = LayoutParams.MATCH_PARENT;
        dlg.getWindow().setAttributes(lp);

        final EditText jml = (EditText) dlg.findViewById(R.id.edJmlPesan);
        final EditText no_meja = (EditText) dlg.findViewById(R.id.edNo_Meja);
        //final String jum = jml.getText().toString();
        Button btPsn = (Button) dlg.findViewById(R.id.btPesan);

        btPsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FormValidation myVal = new FormValidation() {
                    @NotEmpty(message = "Jumlah menu harus diisi")
                    private EditText jml;
                    Validator val;

                    @Override
                    public void setValidator() {
                        jml = (EditText) dlg.findViewById(R.id.edJmlPesan);
                        val = new Validator(this);
                        val.setValidationListener(this);
                        val.validate();
                    }

                    @Override
                    public void onValidationSucceeded() {
                        Toast.makeText(getApplicationContext(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
                        PesanMenu pesanMenu = new PesanMenu();
                        if(count < 1){
                            Log.e("Iblis","Heeh null mang");
                            pesanMenu.init(MenuActivity.this, email, idMenu, no_order, jml.getText().toString(), setOrderNum);
                        }else{
                            Log.e("Iblis","Henteu null mang");
                            pesanMenu.init(MenuActivity.this,email,idMenu,edOrderNum.getText().toString(),jml.getText().toString(),setOrderNum);
                        }
                        count+=1;
                        dlg.dismiss();
                    }

                    @Override
                    public void onValidationFailed(List<ValidationError> errors) {
                        for (ValidationError error : errors) {
                            View view = error.getView();
                            String message = error.getCollatedErrorMessage(getApplicationContext());

                            // Display error messages ;)
                            if (view instanceof EditText) {
                                ((EditText) view).setError(message);
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                };

                myVal.setValidator();

               /*
                PesanMenu pesanMenu = new PesanMenu();
                if(count < 1){
                    //no_order = edOrderNum.getText().toString();
                    Log.e("Iblis","Heeh null mang");
                    pesanMenu.init(MenuActivity.this, email, idMenu, no_order, jml.getText().toString(), setOrderNum);
                }else{
                    Log.e("Iblis","Henteu null mang");
                    pesanMenu.init(MenuActivity.this,email,idMenu,edOrderNum.getText().toString(),jml.getText().toString(),setOrderNum);
                }
                count+=1;
                //pesanMenu.init(MenuActivity.this,email,idMenu,no_order,jml.getText().toString(),setOrderNum);
                //Toast.makeText(getApplicationContext(), idMenu + " " + no_order + " " + jml.getText().toString(), Toast.LENGTH_LONG).show();
                dlg.dismiss();*/
            }
        });
        dlg.show();
    }


    //Load Image untuk detail menu
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image = null;
            String imgUrl = (String) params[0];
            try {
                InputStream is = new java.net.URL(imgUrl).openStream();
                image = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.d("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                Bitmap img = Bitmap.createScaledBitmap(bitmap, 600, 400, true);
                bmImage.setImageBitmap(img);
            }
        }
    }


    public class CekAtuh extends AsyncTask<Object,Object,Object> {
        JSONParser jsonParser;
        ProgressDialog pd;
        Context context;
        JSONObject jsonObject = null;
        String url = "http://192.168.42.48/Kuliner/JSON/cek_no_order.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonParser = new JSONParser();
        }

        @Override
        protected Object doInBackground(Object... params) {
            JSONObject jsonObject = null;
            //String email = "andryavera@gmail.com";
            List<NameValuePair> dataJson = new ArrayList<NameValuePair>();
            dataJson.add(new BasicNameValuePair("email", email));
            try {
                jsonObject = jsonParser.getJsonOBject(url, "POST", dataJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jsonObject != null) {
                //Log.e("Galat", "Teu kapanggih");
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("tmp_order");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        no_order = jsonArray.getJSONObject(i).getString("no_order");
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
        }
    }

}