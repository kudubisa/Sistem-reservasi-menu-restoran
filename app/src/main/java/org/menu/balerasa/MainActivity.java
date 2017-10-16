package org.menu.balerasa;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.altbeacon.beaconreference.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lib.zxing.qrcode.QRCodeEncoder;
import org.menu.balerasa.AmbilData.JsonObjectResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {
    EntitasCart entitasCart;
    EntitasCart entitasBill;
    ArrayList<EntitasCart> dataCart = new ArrayList<EntitasCart>();
    ArrayList<EntitasCart> dataBill = new ArrayList<EntitasCart>();
    SetCV setCV;

    EntitasCusVoucher entitasCusVoucher;
    ArrayList<EntitasCusVoucher> dataCV = new ArrayList<EntitasCusVoucher>();

    Button btMakanan,btMinuman,btDessert,btAppetizer,btCart,btBill;
    ImageButton btSetMeja;
    TextView txtNoMeja;
    TextView cekStatus;
    String email = "";//andryavera@gmail.com";
    String url = "http://192.168.42.48/Kuliner/JSON/cart_services.php";
    String urlBill = "http://192.168.42.48/Kuliner/JSON/bill_services.php";
    String urlVoucher = "http://192.168.42.48/Kuliner/JSON/cek_customer_voucher.php";
    int count = 0;
    int countCart = 0;
    int countV = 0;
    final String PREFS_NAME = "logged";
    final String PREFS2_NAME = "Pemesanan";
    //String no_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set No meja menjadi kosong
        SharedPreferences setMeja = getSharedPreferences(PREFS2_NAME, MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = setMeja.edit();
        editor.putString("no_meja", "");
        editor.commit();



        //Cek Login apa tidak
        if(isLogged()){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            email = settings.getString("email","");
        }else {
            email = getEmail();
        }

        txtNoMeja = (TextView) findViewById(R.id.noMeja);

        btMakanan = (Button) findViewById(R.id.btMakanan);
        btMinuman = (Button) findViewById(R.id.btMinuman);
        btDessert = (Button) findViewById(R.id.btDessert);
        btAppetizer = (Button) findViewById(R.id.btAppetizer);


        cekStatus = (TextView) findViewById(R.id.cekStatus);
        btCart = (Button) findViewById(R.id.btCart);
        btBill = (Button) findViewById(R.id.btBill);

        btSetMeja = (ImageButton) findViewById(R.id.btSetMeja);

        btMakanan.setOnClickListener(this);
        btMinuman.setOnClickListener(this);
        btDessert.setOnClickListener(this);
        btAppetizer.setOnClickListener(this);

        btCart.setOnClickListener(this);
        btBill.setOnClickListener(this);
        btSetMeja.setOnClickListener(this);

        /*CekNoOrder cekNoOrder = new CekNoOrder();
        cekNoOrder.init(MainActivity.this, email, setNorMej);

        if(cekStatus.getText()==""){
           Toast.makeText(getApplicationContext(),"eweh",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"aya "+no_order,Toast.LENGTH_LONG).show();
        }*/

    }



    /*public SetNorMej setNorMej = new SetNorMej() {
        @Override
        public void setOrMej(String no_order, String no_meja) {
            //MainActivity main = new MainActivity();
            //main.no_order = no_order;
            cekStatus.setText(no_order);
        }
    };*/

    //Set No meja Section
    public void setNoMeja() {

        final Dialog dlg = new Dialog(this);
        dlg.setTitle("Silahkan Set No Meja, Hindari Memasukan No Meja Yang Salah!");
        dlg.setContentView(R.layout.dialog_set_meja);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);
        final Spinner spinner = (Spinner) dlg.findViewById(R.id.spinner);
        String no_meja[] = {"M1","M2","M3","M4","M5", "M6","M7","M8","M9","M10"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, no_meja);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);


        final EditText edNoMeja = (EditText) dlg.findViewById(R.id.edNo_Meja);

        Button btSetNoMeja = (Button) dlg.findViewById(R.id.btSetNoMeja);
        SharedPreferences getMeja = getSharedPreferences(PREFS2_NAME, MODE_PRIVATE);
        edNoMeja.setText(getMeja.getString("no_meja", ""));
        spinner.setSelection(getMeja.getInt("position",0));
        btSetNoMeja.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                FormValidation myVal = new FormValidation() {
                    @NotEmpty(message = "Harus Diisi")
                    EditText edNoMeja;
                    private Validator val;
                    @Override
                    public void setValidator() {
                        edNoMeja = (EditText) dlg.findViewById(R.id.edNo_Meja);
                        val = new Validator(this);
                        val.setValidationListener(this);
                        val.validate();
                    }

                    @Override
                    public void onValidationSucceeded() {
                        Toast.makeText(getApplicationContext(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
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

                //myVal.setValidator();
                SharedPreferences setMeja = getSharedPreferences(PREFS2_NAME, MODE_PRIVATE);
                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = setMeja.edit();
                editor.putString("no_meja", spinner.getSelectedItem().toString());
                editor.putInt("position", spinner.getSelectedItemPosition());
                txtNoMeja.setText(spinner.getSelectedItem().toString());

                editor.commit();
                Log.d("noMeja", setMeja.getString("no_meja", ""));

                dlg.dismiss();
            }
        });

        dlg.show();

    }

    public JsonObjectResult jsonObjectResult = new JsonObjectResult() {
        @Override
        public void gotJsonObject(JSONObject object) {
            Log.e("Test2","Enter...");
            try {
                JSONArray jsonArray = object.getJSONArray("datacart");
                int length = jsonArray.length();
                Log.e("Test1","Length = "+length);
                for(int i = 0; i < jsonArray.length();i++){
                    entitasCart = new EntitasCart();
                    entitasCart.setIdMenu(jsonArray.getJSONObject(i).getString("id_menu"));
                    entitasCart.setNamaMenu(jsonArray.getJSONObject(i).getString("nama"));
                    entitasCart.setQtyMenu(jsonArray.getJSONObject(i).getString("jml_order"));
                    entitasCart.setJumHrg(jsonArray.getJSONObject(i).getString("total_order"));
                    entitasCart.setPicMenu(jsonArray.getJSONObject(i).getString("image"));
                    dataCart.add(entitasCart);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tampilCart(dataCart);


        }
    };

    public void tampilCart(final ArrayList<EntitasCart> dataCart){

        final Dialog dlg = new Dialog(this);
        dlg.setTitle("Order Menu Cart");
        dlg.setContentView(R.layout.dialog_cart_listview);
        final ListView lv;
        lv = (ListView) dlg.findViewById(R.id.cartList);
        final CartBaseAdapter cartBaseAdapter = new CartBaseAdapter(MainActivity.this,dataCart);
        lv.setAdapter(cartBaseAdapter);

        if(countCart>0){
            Log.d("LV", "Adapter tidak kosong");
            dataCart.clear();
            CartBaseAdapter cartBaseAdapters = new CartBaseAdapter(MainActivity.this,dataCart);
            lv.setAdapter(cartBaseAdapters);

            AmbilData ambilData = new AmbilData();
            ambilData.init(MainActivity.this, jsonObjectResult, email, url);

            countCart =-1;
            dlg.dismiss();

        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idmenu = dataCart.get(position).getIdMenu();
                deleteMenuOnCart(idmenu);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);


        Button btKembali = (Button) dlg.findViewById(R.id.btKembali);
        Button btKirimPesan = (Button) dlg.findViewById(R.id.btKirPes);
        Button btPilihVoucher = (Button) dlg.findViewById(R.id.btPilihVoucher);
        final EditText edCV = (EditText) dlg.findViewById(R.id.edCodeVoucher);

        btKembali.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCart.clear();
                CartBaseAdapter cartBaseAdapter = new CartBaseAdapter(MainActivity.this,dataCart);
                lv.setAdapter(cartBaseAdapter);
                dlg.dismiss();
            }
        });
        btKirimPesan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                KirimPesanan kirimPesanan = new KirimPesanan();
                kirimPesanan.init(MainActivity.this, email, edCV.getText().toString());
            }
        });
        btPilihVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilData ad = new AmbilData();
                ad.init(MainActivity.this, jsonObjectResVoucher, email, urlVoucher);
            }
        });

        SetCV setCoVo = new SetCV() {
            @Override
            public void setCV(String code) {
                edCV.setText(code);
            }
        };
        setCV = setCoVo;
        if(countCart != -1) {
            dlg.show();
        }
        countCart+=1;
    }

    public void deleteMenuOnCart(final String idMenu){
        final Dialog dlg = new Dialog(this);
        dlg.setTitle("Hapus menu dari keranjang?");
        dlg.setContentView(R.layout.dialog_delete_cart);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        dlg.getWindow().setAttributes(lp);

        Button btKembali = (Button) dlg.findViewById(R.id.btKembali);
        Button btDelete = (Button) dlg.findViewById(R.id.btDelete);

        btKembali.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        btDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                DeleteCart deleteCart = new DeleteCart();
                deleteCart.init(MainActivity.this, email, idMenu);
            }
        });
        dlg.show();
    }



    public JsonObjectResult jsonObjectResultBill = new JsonObjectResult() {
        @Override
        public void gotJsonObject(JSONObject object) {
            int jumlah=0;
            Log.e("Test2","Enter...");
            try {
                JSONArray jsonArray = object.getJSONArray("datacart");
                int length = jsonArray.length();
                Log.e("Test1","Length = "+length);
                for(int i = 0; i < jsonArray.length();i++){
                    entitasBill = new EntitasCart();
                    entitasBill.setIdMenu(jsonArray.getJSONObject(i).getString("id_menu"));
                    entitasBill.setNamaMenu(jsonArray.getJSONObject(i).getString("nama"));
                    entitasBill.setQtyMenu(jsonArray.getJSONObject(i).getString("jml_order"));
                    entitasBill.setJumHrg(jsonArray.getJSONObject(i).getString("total_order"));
                    entitasBill.setPicMenu(jsonArray.getJSONObject(i).getString("image"));
                    dataBill.add(entitasBill);
                    jumlah += jsonArray.getJSONObject(i).getInt("total_order");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tampilBill(dataBill, jumlah);

        }
    };

    public void tampilBill(ArrayList<EntitasCart> dataBill, int jumlah){
        final Dialog dlg = new Dialog(this);
        dlg.setTitle("Tagihan Anda");
        dlg.setContentView(R.layout.dialog_bill_listview);
        final ListView lv;
        lv = (ListView) dlg.findViewById(R.id.billList);
        CartBaseAdapter cartBaseAdapter;
        cartBaseAdapter = new CartBaseAdapter(MainActivity.this,dataBill);

        if(count>0){
            Log.d("LV", "Adapter tidak kosong");
            dataBill.clear();
            CartBaseAdapter cartBaseAdapters = new CartBaseAdapter(MainActivity.this,dataBill);
            lv.setAdapter(cartBaseAdapters);

            AmbilData ambilData2 = new AmbilData();
            ambilData2.init(MainActivity.this, jsonObjectResultBill, email, urlBill);

            count =-1;
            dlg.dismiss();

        }

        lv.setAdapter(cartBaseAdapter);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);




        Button btKembali = (Button) dlg.findViewById(R.id.btKembali);
        Button btBayar = (Button) dlg.findViewById(R.id.btBayar);
        TextView tvTotal = (TextView) dlg.findViewById(R.id.tvTotal);
        tvTotal.setText(Integer.toString(jumlah));

        btKembali.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        btBayar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                //QRCode Script
                //Find screen size
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3/4;

                showQRCode(email,smallerDimension);

            }
        });
        if(count != -1) {
            dlg.show();
        }
        count+=1;


    }

    public JsonObjectResult jsonObjectResVoucher = new JsonObjectResult() {
        @Override
        public void gotJsonObject(JSONObject object) {
            Log.e("Test2","Enter...");
            try {
                JSONArray jsonArray = object.getJSONArray("datavc");
                int length = jsonArray.length();
                Log.e("Test1","Length = "+length);
                for(int i = 0; i < jsonArray.length();i++){
                    entitasCusVoucher = new EntitasCusVoucher();
                    entitasCusVoucher.setCode(jsonArray.getJSONObject(i).getString("code_voucher"));
                    entitasCusVoucher.setDes(jsonArray.getJSONObject(i).getString("deskripsi"));
                    entitasCusVoucher.setExpired(jsonArray.getJSONObject(i)
                            .getString("expired"));
                    dataCV.add(entitasCusVoucher);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tampilVoucher(dataCV);

        }
    };

    public abstract class SetCV{
        public abstract void setCV(String code);
    }

    public void tampilVoucher(final ArrayList<EntitasCusVoucher> data){
        final Dialog dlg = new Dialog(this);
        dlg.setTitle("Voucher Anda");
        dlg.setContentView(R.layout.dialog_voucher_listview);
        final ListView lv;
        lv = (ListView) dlg.findViewById(R.id.voucherList);
        VoucherBaseAdapter voucherBaseAdapter;
        voucherBaseAdapter = new VoucherBaseAdapter(MainActivity.this,data);

        if(countV>0){
            Log.d("LV", "Adapter tidak kosong");
            data.clear();
            voucherBaseAdapter = new VoucherBaseAdapter(MainActivity.this,data);
            lv.setAdapter(voucherBaseAdapter);
            AmbilData ad2 = new AmbilData();
            ad2.init(MainActivity.this, jsonObjectResVoucher, email, urlVoucher);

            countV =-1;
            dlg.dismiss();

        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCV.setCV(data.get(position).getCode());
                dlg.dismiss();
            }
        });


        lv.setAdapter(voucherBaseAdapter);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);




        Button btKembali = (Button) dlg.findViewById(R.id.btKembali);

        btKembali.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        if(countV != -1) {
            dlg.show();
        }
        countV+=1;


    }



    private void showQRCode(String text, int dmn){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Kode Tagihan Anda");
        dialog.setContentView(R.layout.show_qrcode_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        ImageView myImage = (ImageView) dialog.findViewById(R.id.imageView1);

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(text,
                null,
                com.google.zxing.client.android.Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                dmn);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();

            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        Button btBack = (Button) dialog.findViewById(R.id.btBack);

        btBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public boolean isLogged(){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Reading from SharedPreferences
        int value = settings.getInt("isLoggedIn", 0);
        String email = settings.getString("email","");
        Log.d("email", email);
        if(value == 1) {
            return true;
        }else{
            return false;
        }
    }

    //Get Primary Email from Device
    public String getEmail(){
        String primaryEmail="";

        try{
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

            for (Account account : accounts) {
                primaryEmail = account.name;
            }
        }
        catch(Exception e)
        {
            Log.i("Exception", "Exception:" + e) ;
        }

        Log.i("Exception", "mails:" + primaryEmail) ;

        return primaryEmail;
    }



    //Handle Button
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btMinuman:
                if(txtNoMeja.getText().equals("-")){
                    Toast.makeText(getApplicationContext(),"Isi terlebih dahulu no meja sebelum memesan menu!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Bundle bn = new Bundle();
                    bn.putString("kategori", "1");
                    bn.putString("email", email);
                    //bn.putString("no_order",cekStatus.getText().toString());
                    Intent in = new Intent(MainActivity.this,MenuActivity.class);
                    in.putExtras(bn);
                    startActivity(in);
                }
                //this.finish();
                break;
            case R.id.btMakanan:
                if(txtNoMeja.getText().equals("-")){
                    Toast.makeText(getApplicationContext(),"Isi terlebih dahulu no meja sebelum memesan menu!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Bundle b = new Bundle();
                    b.putString("kategori", "2");
                    b.putString("email", email);
                    //b.putString("no_order",cekStatus.getText().toString());

                    Intent i = new Intent(MainActivity.this,MenuActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                }

                //this.finish();
                break;

            case R.id.btDessert:
                if(txtNoMeja.getText().equals("-")){
                    Toast.makeText(getApplicationContext(),"Isi terlebih dahulu no meja sebelum memesan menu!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Bundle bu = new Bundle();
                    bu.putString("kategori", "3");
                    bu.putString("email", email);
                    //b.putString("no_order",cekStatus.getText().toString());

                    Intent it = new Intent(MainActivity.this,MenuActivity.class);
                    it.putExtras(bu);
                    startActivity(it);
                }
                //this.finish();
                break;

            case R.id.btAppetizer:
                if(txtNoMeja.getText().equals("-")){
                    Toast.makeText(getApplicationContext(),"Isi terlebih dahulu no meja sebelum memesan menu!",
                            Toast.LENGTH_LONG).show();
                }else {
                    Bundle bun = new Bundle();
                    bun.putString("kategori", "4");
                    bun.putString("email", email);
                    //b.putString("no_order",cekStatus.getText().toString());

                    Intent itn = new Intent(MainActivity.this, MenuActivity.class);
                    itn.putExtras(bun);
                    startActivity(itn);
                }
                //this.finish();
                break;

            case R.id.btCart:
                AmbilData ambilData = new AmbilData();
                ambilData.init(MainActivity.this, jsonObjectResult, email, url);
                //this.finish();
                break;

            case R.id.btBill:
                AmbilData ambilData2 = new AmbilData();
                ambilData2.init(MainActivity.this,jsonObjectResultBill,email,urlBill);
                break;
            case R.id.btSetMeja:
                setNoMeja();
                break;

        }
    }



}

