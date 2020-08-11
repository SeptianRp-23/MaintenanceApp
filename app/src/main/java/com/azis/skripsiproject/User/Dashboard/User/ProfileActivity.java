package com.azis.skripsiproject.User.Dashboard.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.azis.skripsiproject.Admin.Laporan.AdmLaporanActivity;
import com.azis.skripsiproject.Controller.DataItemAdmin;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;
import com.azis.skripsiproject.User.Dashboard.Pengajuan.PilihPenggunaActivity;
import com.azis.skripsiproject.User.Proses.ProsesStatusActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtNama, txtTelp, txtMail, txtAlamat;
    private String GetUserAPI = Api.URL_API + "dataUser.php";
    private String getID;
    private Button Logout;
    private Toast backToast;
    SharedPreferences sharedPreferences;
    private long backPressedTime;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);
        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        txtNama=findViewById(R.id.txt_nama_user);
        txtTelp  =findViewById(R.id.txt_telp_user);
        txtMail  = findViewById(R.id.txt_mail_user);
        txtAlamat =  findViewById(R.id.txt_alamat_user);
        Logout = findViewById(R.id.btn_logout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setMessage("Tunggu Sebentar . . .");
                progressDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Logout();
                    }
                }, 3000);
            }
        });

        //ButtomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),
                                DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.status:
                        startActivity(new Intent(getApplicationContext(),
                                ProsesStatusActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id. account:
//                        startActivity(new Intent(getApplicationContext(),
//                                ProfileActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //End ButtomNav
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

                                    txtNama.setText(strName);
                                    txtTelp.setText(strTelp);
                                    txtMail.setText(strEmail);
                                    txtAlamat.setText(strAlamat);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Bermasalah! "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Koneksi Error! "+error.toString(), Toast.LENGTH_SHORT).show();
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

    private void Logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
        editor.apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();
            finish();
        } else {
            backToast = Toast.makeText(this, "Tekan Lagi Untuk Keluar", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}