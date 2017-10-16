package org.altbeacon.service;

/**
 * Created by Andrya on 1/22/2016.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.altbeacon.beaconreference.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.menu.balerasa.FormValidation;
import org.menu.balerasa.MainActivity;

import java.util.List;

public class KonfigurasiPenggunaActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] menu;

    public boolean isLogged(){
        final String PREFS_NAME = "logged";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Reading from SharedPreferences
        int value = settings.getInt("isLoggedIn", 0);
        String email = settings.getString("email","");
        Log.d("email",email);
        if(value == 1) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        mTitle = mDrawerTitle = getTitle();
        if(isLogged()){
            menu = getResources().getStringArray(R.array.array_menu2);
            Log.d("berhasil","yuhuuu");

        }else{
            menu = getResources().getStringArray(R.array.array_menu1);
            Log.d("gagal","kampret");
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menu));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else{
            return false;
        }

    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments

        Fragment fragment;
        Bundle args = new Bundle();
        if(isLogged()) {
            if (position == 3) {
                final String PREFS_NAME = "logged";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("isLoggedIn", 0);
                editor.commit();
                //intent for logout
                Intent myIntent = new Intent(KonfigurasiPenggunaActivity.this, KonfigurasiPenggunaActivity.class);
                //myIntent.putExtra("RecentProblemId", result);
                startActivity(myIntent);
                this.finish();
            }else if(position == 2){
                final String PREFS_NAME = "OnSite";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                int onSite = settings.getInt("isOnSite", 0);
                if(onSite==1){

                    Intent myIntent = new Intent(KonfigurasiPenggunaActivity.this, MainActivity.class);
                    //myIntent.putExtra("RecentProblemId", result);
                    this.finish();
                    startActivity(myIntent);
                }else{
                    Toast.makeText(this,"Anda tidak berada di area restoran!",Toast.LENGTH_SHORT).show();
                }
            }else {
                fragment = new KonfigurasiPengguna2();
                args.putInt(KonfigurasiPengguna2.ARG_POSITION, position);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                // update selected item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(menu[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
                Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
            }
        }else{
            fragment = new KonfigurasiPengguna1();
            args.putInt(KonfigurasiPengguna1.ARG_POSITION, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(menu[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class KonfigurasiPengguna1 extends Fragment{
        public static final String ARG_POSITION = "number";

        public KonfigurasiPengguna1(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_POSITION);

            final View rootView;
            switch(i){
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_login, container, false);
                    Button btLogin = (Button) rootView.findViewById(R.id.btLogin);
                    final EditText edEmail, edPass;
                    edEmail = (EditText) rootView.findViewById(R.id.edEmail);
                    edPass = (EditText) rootView.findViewById(R.id.edPassword);
                    btLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FormValidation myVal = new FormValidation() {

                                @NotEmpty(message = "Email wajib diisi")
                                @Email(message = "Email yang anda masukan tidak valid")
                                private EditText edEmail;

                                @NotEmpty(message = "Password wajib diisi")
                                @Password(message = "Password tidak valid, karakter min 6",min = 6)
                                private EditText edPass;

                                private Validator val;


                                @Override
                                public void setValidator() {

                                    edEmail = (EditText) rootView.findViewById(R.id.edEmail);
                                    edPass = (EditText) rootView.findViewById(R.id.edPassword);

                                    val = new Validator(this);
                                    val.setValidationListener(this);
                                    val.validate();
                                }

                                @Override
                                public void onValidationSucceeded() {
                                    Toast.makeText(getActivity(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
                                    Login.Success success = new Login.Success() {
                                        @Override
                                        public void loginStat(String result) {
                                            if(result != null){
                                                getActivity().finish();
                                            }
                                        }
                                    };
                                    //Toast.makeText(getActivity(),"Pressed",Toast.LENGTH_SHORT).show();
                                    Login lg = new Login();
                                    lg.init(getActivity(),edEmail.getText().toString(),edPass.getText().toString(),success);
                                }

                                @Override
                                public void onValidationFailed(List<ValidationError> errors) {
                                    for (ValidationError error : errors) {
                                        View view = error.getView();
                                        String message = error.getCollatedErrorMessage(getActivity());

                                        // Display error messages ;)
                                        if (view instanceof EditText) {
                                            ((EditText) view).setError(message);
                                        } else {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            };
                            myVal.setValidator();


                        }
                    });



                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_register, container, false);
                    Button btRegister = (Button) rootView.findViewById(R.id.btRegsiter);
                    final EditText edEm, edPas1, edPas2, edNama,edAlamat,edNoTelp;
                    edEm = (EditText) rootView.findViewById(R.id.edEmail);
                    edPas1 = (EditText) rootView.findViewById(R.id.edPassword1);
                    edPas2 = (EditText) rootView.findViewById(R.id.edPassword2);
                    edNama = (EditText) rootView.findViewById(R.id.edNama);
                    edAlamat = (EditText) rootView.findViewById(R.id.edAlamat);
                    edNoTelp = (EditText) rootView.findViewById(R.id.edNoTelp);

                    btRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FormValidation myVal = new FormValidation() {
                                @NotEmpty(message = "Email wajib diisi")
                                @Email(message = "Email yang anda masukan tidak valid")
                                EditText edEm;

                                @NotEmpty(message = "Password wajib diisi")
                                @Password(message = "Password min 6 karakter",min = 6)
                                EditText edPas1;

                                @NotEmpty(message = "Password konfirmasi wajib diisi")
                                @ConfirmPassword(message = "Password konfirmasi tidak sama")
                                EditText edPas2;

                                @NotEmpty(message="Nama wajib diisi")
                                EditText edNama;

                                @NotEmpty(message = "Alamat wajib diisi")
                                EditText edAlamat;

                                @NotEmpty(message = "No Telpon wajib diisi")
                                EditText edNoTelp;

                                private Validator val;

                                @Override
                                public void setValidator() {
                                    edEm = (EditText) rootView.findViewById(R.id.edEmail);
                                    edPas1 = (EditText) rootView.findViewById(R.id.edPassword1);
                                    edPas2 = (EditText) rootView.findViewById(R.id.edPassword2);
                                    edNama = (EditText) rootView.findViewById(R.id.edNama);
                                    edAlamat = (EditText) rootView.findViewById(R.id.edAlamat);
                                    edNoTelp = (EditText) rootView.findViewById(R.id.edNoTelp);

                                    val = new Validator(this);
                                    val.setValidationListener(this);
                                    val.validate();
                                }

                                @Override
                                public void onValidationSucceeded() {
                                    Toast.makeText(getActivity(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
                                    String pass1="";
                                    pass1=edPas1.getText().toString();
                                    String em = "";
                                    String nama = "";
                                    String almt = "";
                                    String notelp = "";
                                    em = edEm.getText().toString();
                                    nama = edNama.getText().toString();
                                    almt = edAlamat.getText().toString();
                                    notelp = edNoTelp.getText().toString();

                                    Register rg = new Register();
                                    Register.Success success = new Register.Success() {
                                        @Override
                                        public void refresh() {
                                            getActivity().recreate();
                                            edEm.setText("");
                                            edPas1.setText("");
                                            edPas2.setText("");
                                            edNama.setText("");
                                            edAlamat.setText("");
                                            edNoTelp.setText("");
                                            Toast.makeText(getActivity(),"Silahkan melakukan login!",Toast.LENGTH_SHORT).show();
                                        }
                                    };
                                    rg.init(getActivity(),em,pass1,"",nama,almt,notelp,success);

                                }

                                @Override
                                public void onValidationFailed(List<ValidationError> errors) {
                                    for (ValidationError error : errors) {
                                        View view = error.getView();
                                        String message = error.getCollatedErrorMessage(getActivity());

                                        // Display error messages ;)
                                        if (view instanceof EditText) {
                                            ((EditText) view).setError(message);
                                        } else {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            };
                            myVal.setValidator();
                        }
                    });


                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    break;
            }

            //return super.onCreateView(inflater, container, savedInstanceState);
            return rootView;
        }
    }

    public static class KonfigurasiPengguna2 extends Fragment{
        public static final String ARG_POSITION = "number";

        public KonfigurasiPengguna2(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_POSITION);

            final View rootView;
            switch(i){
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_profile, container, false);
                    Context context = getActivity();
                    final EditText edEmail,edPass,edPB,edPB2,edNama,edAlmt,edNoTelp;
                    TextView tvGantiPass;
                    Button btSimpan = (Button) rootView.findViewById(R.id.btSaveProfile);
                    edEmail = (EditText) rootView.findViewById(R.id.edEmail);
                    edPass = (EditText) rootView.findViewById(R.id.edPassword);
                    edNama= (EditText) rootView.findViewById(R.id.edNama);
                    edAlmt = (EditText) rootView.findViewById(R.id.edAlamat);
                    edNoTelp = (EditText) rootView.findViewById(R.id.edNoTelp);
                    tvGantiPass = (TextView) rootView.findViewById(R.id.tvGantiPassword);
                    edPB = (EditText) rootView.findViewById(R.id.edPasswordBaru);
                    edPB2 = (EditText) rootView.findViewById(R.id.edPasswordBaru2);
                    /*final String PREFS_NAME = "logged";
                    
                    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    edEmail.setText(settings.getString("email",""));*/
                    AmbilProfile.JSONObjectResultAP jsonObjectResultAP = new AmbilProfile.JSONObjectResultAP() {
                        @Override
                        public void gotJSONObject(JSONObject jsonObject) {
                            try {
                                JSONArray arrayMenu = jsonObject.getJSONArray("datacustomer");
                                for (int i = 0; i < arrayMenu.length(); i++) {
                                    edEmail.setText(arrayMenu.getJSONObject(i)
                                            .getString("email"));
                                    edPass.setText(arrayMenu.getJSONObject(i)
                                            .getString("password"));
                                    edNama.setText(arrayMenu.getJSONObject(i)
                                            .getString("nama"));
                                    edAlmt.setText(arrayMenu.getJSONObject(i)
                                            .getString("alamat"));
                                    edNoTelp.setText(arrayMenu.getJSONObject(i)
                                            .getString("no_telp"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    AmbilProfile ap = new AmbilProfile();
                    ap.init(context,jsonObjectResultAP);

                    //Ganti Password Cliked


                    tvGantiPass.setOnClickListener(new View.OnClickListener() {
                        int click = 0;
                        String oldPass = edPass.getText().toString();
                        @Override
                        public void onClick(View v) {
                            if(click == 0){
                                Toast.makeText(getActivity(),"Ganti Password Yuk!",Toast.LENGTH_SHORT).show();
                                edPass.setText("");
                                edPB.setText("");
                                edPB2.setText("");
                                edPass.setVisibility(View.VISIBLE);
                                edPB.setVisibility(View.VISIBLE);
                                edPB2.setVisibility(View.VISIBLE);
                                click++;
                            }else{
                                click = 0;
                                edPass.setVisibility(View.GONE);
                                edPB.setVisibility(View.GONE);
                                edPB2.setVisibility(View.GONE);
                            }

                        }
                    });

                    btSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FormValidation myVal = new FormValidation() {

                                @NotEmpty(message = "Email wajib diisi")
                                @Email(message = "Email yang anda masukan tidak valid")
                                EditText edEm;

                                @NotEmpty(message = "Password lama wajib diisi")
                                EditText edPass;

                                @NotEmpty(message = "Password baru wajib diisi")
                                @Password(message = "Password min 6 karakter",min = 6)
                                EditText edPB;

                                @NotEmpty(message = "Password konfirmasi wajib diisi")
                                @ConfirmPassword(message = "Password konfirmasi tidak sama")
                                EditText edPB2;

                                @NotEmpty(message="Nama wajib diisi")
                                EditText edNama;

                                @NotEmpty(message = "Alamat wajib diisi")
                                EditText edAlamat;

                                @NotEmpty(message = "No Telpon wajib diisi")
                                EditText edNoTelp;

                                private Validator val;

                                @Override
                                public void setValidator() {
                                    edEm = (EditText) rootView.findViewById(R.id.edEmail);
                                    edPass = (EditText) rootView.findViewById(R.id.edPassword);
                                    edPB = (EditText) rootView.findViewById(R.id.edPasswordBaru);
                                    edPB2 = (EditText) rootView.findViewById(R.id.edPasswordBaru2);
                                    edNama = (EditText) rootView.findViewById(R.id.edNama);
                                    edAlamat = (EditText) rootView.findViewById(R.id.edAlamat);
                                    edNoTelp = (EditText) rootView.findViewById(R.id.edNoTelp);

                                    val = new Validator(this);
                                    val.setValidationListener(this);
                                    val.validate();
                                }

                                @Override
                                public void onValidationSucceeded() {
                                    Toast.makeText(getActivity(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
                                    Register rg = new Register();

                                    Register.Success success = new Register.Success() {
                                        @Override
                                        public void refresh() {
                                            getActivity().recreate();
                                        }
                                    };

                                    rg.init(getActivity(),edEmail.getText().toString(),edPass.getText().toString()
                                            ,edPB.getText().toString(),edNama.getText().toString(),edAlmt.getText().toString()
                                            ,edNoTelp.getText().toString(),success);
                                }

                                @Override
                                public void onValidationFailed(List<ValidationError> errors) {
                                    for (ValidationError error : errors) {
                                        View view = error.getView();
                                        String message = error.getCollatedErrorMessage(getActivity());

                                        // Display error messages ;)
                                        if (view instanceof EditText) {
                                            ((EditText) view).setError(message);
                                        } else {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            };
                            myVal.setValidator();

                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);

                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    break;
            }

            //return super.onCreateView(inflater, container, savedInstanceState);
            return rootView;
        }
    }

}