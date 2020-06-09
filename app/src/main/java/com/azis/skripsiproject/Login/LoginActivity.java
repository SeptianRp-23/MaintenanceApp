package com.azis.skripsiproject.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Admin.Dashboard.AdmDashboardActivity;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button btLogin;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    CheckBox ceklist;
    RelativeLayout btRegist;
    private String LoginAPI = Api.URL_API + "login.php";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);


        etEmail = findViewById(R.id.log_email);
        etPass = findViewById(R.id.log_pass);
        btLogin = findViewById(R.id.btnLogin);
        ceklist = findViewById(R.id.state);
        btRegist = findViewById(R.id.register);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = etEmail.getText().toString().trim();
                String mPass = etPass.getText().toString().trim();

                if (etEmail.getText().toString().length()==0){
                    etEmail.setError("Null Email");
                }
                else if (etPass.getText().toString().length()==0){
                    etPass.setError("Null Password");
                }
                else{

                    loginProces(mEmail, mPass);
                }
            }
        });
        String loginstatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");
        if (loginstatus.equals("LoggedIn")){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        }
        else if (loginstatus.equals("LoggedOn")){
            startActivity(new Intent(LoginActivity.this, AdmDashboardActivity.class));
        }
    }

    private void loginProces(final String email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        final Handler handler = new Handler();
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginAPI,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                                    if (success.equals("1")) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String nama = object.getString("nama").trim();
                                            String email = object.getString("email").trim();
                                            String bagian = object.getString("bagian").trim();
                                            String level = object.getString("level").trim();
                                            String id = object.getString("id").trim();

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            if (ceklist.isChecked() && level.equals("user")){
                                                editor.putString(getResources().getString(R.string.prefLoginState),"LoggedIn");
                                            }else if (ceklist.isChecked() && level.equals("admin")){
                                                editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOn");
                                            }else{
                                                editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
                                            }

                                            if (level.equals("user")) {
                                                sessionManager.createSession(nama, email, bagian, level, id);
                                                editor.apply();
                                                final Intent inte = new Intent(LoginActivity.this, DashboardActivity.class);
                                                inte.putExtra("bagian", bagian);
                                                inte.putExtra("email", email);
                                                startActivity(inte);
                                                finish();
                                            }else if (level.equals("admin")){
                                                sessionManager.createSession(nama, email, bagian, level, id);
                                                editor.apply();
                                                final Intent in = new Intent(LoginActivity.this, AdmDashboardActivity.class);
                                                in.putExtra("nama", nama);
                                                in.putExtra("email", email);
                                                startActivity(in);
                                                finish();
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Error, Email Or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error, Check Connection" +error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
            }
        }, 2000);

    }

    @Override
    public void onBackPressed() {
        progressDialog.dismiss();
    }

//    private void Login(final String email, final String password) {
//        loading.setVisibility(View.VISIBLE);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "URL_LOGIN_USER",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            JSONArray jsonArray = jsonObject.getJSONArray("login");
//
//                            if (success.equals("1")) {
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject object = jsonArray.getJSONObject(i);
//
//                                    String name = object.getString("name").trim();
//                                    String email = object.getString("email").trim();
//                                    String level = object.getString("level").trim();
//                                    String id = object.getString("id").trim();
//
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    if (ceklist.isChecked() && level.equals("user")){
//                                        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedIn");
//                                    }else if (ceklist.isChecked() && level.equals("admin")){
//                                        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOn");
//                                    }
//                                    else {
//                                        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
//                                    }
//
//                                    if (level.equals("user")){
//                                        sessionManager.createSession(name, email, level, id);
//                                        editor.apply();
//                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//                                        intent.putExtra("name", name);
//                                        intent.putExtra("email", email);
//                                        startActivity(intent);
//                                        finish();
//
//                                    }
//                                    else if (level.equals("admin")){
//                                        sessionManager.createSession(name, email, level, id);
//                                        editor.apply();
//                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//                                        intent.putExtra("name", name);
//                                        intent.putExtra("email", email);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            loading.setVisibility(View.GONE);
//                            btn_login.setVisibility(View.VISIBLE);
//                            Toast.makeText(LoginActivity.this, "Error, Email Or Password", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        loading.setVisibility(View.GONE);
//                        btn_login.setVisibility(View.VISIBLE);
//                        Toast.makeText(LoginActivity.this, "Error, Check Connection" +error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("email", email);
//                params.put("password", password);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
}