package com.azis.skripsiproject.Admin.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Admin.Dashboard.AdmDashboardActivity;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;
import com.azis.skripsiproject.User.Dashboard.User.ProfileActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdmAkunActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String getID;
    Button btLogout, tbSimpanAdd, btSimpanEdit;
    ImageView btBack, btEdit, btAdd;
    MaterialEditText mtNama, mtEmail, mtTelp, mtAlamat, mNama, mEmail, mTelp, mAlamat, mPass;
    LinearLayout linEdit, linAdd;
    private String GetUserAPI = Api.URL_API + "dataUser.php";
    private String EditAPI = Api.URL_API + "editUser.php";
    private String InsertAPI = Api.URL_API + "register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_akun);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);

        mtNama=findViewById(R.id.nama);
        mtTelp=findViewById(R.id.telp);
        mtEmail=findViewById(R.id.email);
        mtAlamat=findViewById(R.id.alamat);
        btAdd=findViewById(R.id.add_admin);
        btBack=findViewById(R.id.kembali);
        btEdit=findViewById(R.id.edit_admin);
        btSimpanEdit=findViewById(R.id.simpanEdit);
        tbSimpanAdd=findViewById(R.id.simpanAdd);
        linAdd=findViewById(R.id.linearAdd);
        linEdit=findViewById(R.id.linearEdit);
        mAlamat=findViewById(R.id.add_alamat);
        mNama=findViewById(R.id.add_nama);
        mEmail=findViewById(R.id.add_email);
        mTelp=findViewById(R.id.add_telp);
        mPass=findViewById(R.id.add_pass);

        mtAlamat.setEnabled(false);
        mtEmail.setEnabled(false);
        mtTelp.setEnabled(false);
        mtNama.setEnabled(false);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdmAkunActivity.this, AdmDashboardActivity.class));
                overridePendingTransition(0,0);
                finishAffinity();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linAdd.setVisibility(View.VISIBLE);
                linEdit.setVisibility(View.GONE);
                btSimpanEdit.setVisibility(View.GONE);
                tbSimpanAdd.setVisibility(View.VISIBLE);
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtEmail.setEnabled(true);
                mtTelp.setEnabled(true);
                mtAlamat.setEnabled(true);
                mtNama.setEnabled(true);
                btSimpanEdit.setVisibility(View.VISIBLE);
                tbSimpanAdd.setVisibility(View.GONE);
                linEdit.setVisibility(View.VISIBLE);
                linAdd.setVisibility(View.GONE);
            }
        });

        tbSimpanAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mNama.getText()) || TextUtils.isEmpty(mtEmail.getText()) || TextUtils.isEmpty(mtTelp.getText())
                        || TextUtils.isEmpty(mAlamat.getText()) || TextUtils.isEmpty(mPass.getText()) ) {
                    Toast.makeText(AdmAkunActivity.this, "Data Belum Lengkap!", Toast.LENGTH_SHORT).show();
                } else {
                    InsertData();
                }
            }
        });

        btSimpanEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mtNama.getText()) || TextUtils.isEmpty(mtAlamat.getText()) || TextUtils.isEmpty(mtEmail.getText())
                || TextUtils.isEmpty(mtTelp.getText())){
                    Toast.makeText(AdmAkunActivity.this, "Data Kosong!", Toast.LENGTH_SHORT).show();
                }else {
                    SaveEditDetail();
                }
            }
        });


    }

    private void getUserDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GetUserAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strId = object.getString("id").trim();
                                    String strName = object.getString("nama").trim();
                                    String strEmail = object.getString("email").trim();
                                    String strTelp = object.getString("telp").trim();
                                    String strAlamat = object.getString("alamat").trim();

                                    mtNama.setText(strName);
                                    mtTelp.setText(strTelp);
                                    mtEmail.setText(strEmail);
                                    mtAlamat.setText(strAlamat);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(AdmAkunActivity.this, "Bermasalah! "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AdmAkunActivity.this, "Koneksi Error! "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    private void SaveEditDetail() {
        final String nama = this.mtNama.getText().toString().trim();
        final String email = this.mtEmail.getText().toString().trim();
        final String telp = this.mtTelp.getText().toString().trim();
        final String alamat = this.mtAlamat.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EditAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(AdmAkunActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdmAkunActivity.this, AdmDashboardActivity.class);
                                startActivity(intent);
//                                sessionManager.createSession(email, name, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(AdmAkunActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AdmAkunActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("alamat", alamat);
                params.put("telp", telp);
                params.put("id", getID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void InsertData() {
        final String txtNama = mNama.getText().toString().trim();
        final String txtEmail = mEmail.getText().toString().trim();
        final String txtTelp = mTelp.getText().toString().trim();
        final String txtProv = "";
        final String txtKota = "";
        final String txtbagian = "";
        final String txtLevel = "admin";
        final String txtAlamat = mAlamat.getText().toString().trim();
        final String txtPass = mPass.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(AdmAkunActivity.this);
        progressDialog.setMessage("Loading . . .");

            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, InsertAPI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
                                Toast.makeText(AdmAkunActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(AdmAkunActivity.this, AdmDashboardActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AdmAkunActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AdmAkunActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nama", txtNama);
                    params.put("email", txtEmail);
                    params.put("provinsi", txtProv);
                    params.put("kota", txtKota);
                    params.put("alamat", txtAlamat);
                    params.put("telp", txtTelp);
                    params.put("bagian", txtbagian);
                    params.put("level", txtLevel);
                    params.put("password", txtPass);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AdmAkunActivity.this);
            requestQueue.add(request);

    }

    private void Logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
        editor.apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}