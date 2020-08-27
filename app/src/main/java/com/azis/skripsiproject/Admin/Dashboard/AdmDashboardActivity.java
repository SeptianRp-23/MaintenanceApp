package com.azis.skripsiproject.Admin.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Admin.LaporPerbaikan.AdmLaporanPerbaikan;
import com.azis.skripsiproject.Admin.Laporan.AdmLaporanActivity;
import com.azis.skripsiproject.Admin.Laporan.PDFActivity;
import com.azis.skripsiproject.Admin.Peminjaman.AdmPeminjamanActivity;
import com.azis.skripsiproject.Admin.Perbaikan.AdmPerbaikanBmnActivity;
import com.azis.skripsiproject.Admin.Profile.AdmAkunActivity;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.User.Dashboard.User.ProfileActivity;

import java.util.HashMap;

import static com.azis.skripsiproject.Server.Api.URL_PDF;

public class AdmDashboardActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String getID;
    Button btLogout;
    ImageView btProfil;
    CardView card1, card2, card3, card4;
    private String createdData1 = URL_PDF + "data_barang.php";
    private String createdData2 = URL_PDF + "pekerjaan_selesai.php";
    private String createdData3 = URL_PDF + "penggunaan_barang.php";
    private String createdData4 = URL_PDF + "perbaikan_bmn.php";
    private String createdData5 = URL_PDF + "perbaikan_famum.php";
    private String createdData6 = URL_PDF + "semua_perbaikan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_dashboard);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        card1 = findViewById(R.id.card_perbaikan);
        card2 = findViewById(R.id.card_peminjaman);
        card3 = findViewById(R.id.card_perbaikan_famum);
        card4 = findViewById(R.id.card_laporan);
        btProfil = findViewById(R.id.profile);


        final MediaPlayer laporan = MediaPlayer.create(this, R.raw.laporan);
        final MediaPlayer profile = MediaPlayer.create(this, R.raw.menuprofile);
        final MediaPlayer perbaikanbmn = MediaPlayer.create(this, R.raw.menupengajuanperbaikan);
        final MediaPlayer peminjaman = MediaPlayer.create(this, R.raw.penggunaanbmn);
        final MediaPlayer laporanperbaikan = MediaPlayer.create(this, R.raw.menulaporanperbaikan);
        final MediaPlayer keluar = MediaPlayer.create(this, R.raw.keluaraplikasi);

        btProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.start();
                startActivity(new Intent(AdmDashboardActivity.this, AdmAkunActivity.class));
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perbaikanbmn.start();
                startActivity(new Intent(AdmDashboardActivity.this, AdmPerbaikanBmnActivity.class));
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peminjaman.start();
                startActivity(new Intent(AdmDashboardActivity.this, AdmPeminjamanActivity.class));
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laporanperbaikan.start();
                startActivity(new Intent(AdmDashboardActivity.this, AdmLaporanPerbaikan.class));
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePDF();
                laporan.start();
                final ProgressDialog progressDialog = new ProgressDialog(AdmDashboardActivity.this);
                progressDialog.setMessage("Tunggu Sebentar . . .");
                progressDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        startActivity(new Intent(AdmDashboardActivity.this, AdmLaporanActivity.class));
                    }
                }, 5000);
            }
        });

        btLogout = findViewById(R.id.logout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keluar.start();
                final ProgressDialog progressDialog = new ProgressDialog(AdmDashboardActivity.this);
                progressDialog.setMessage("Tunggu Sebentar . . .");
                progressDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logout();
                    }
                }, 3000);
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        else {
            backToast = Toast.makeText(this, "Tekan Lagi Untuk Keluar", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void CreatePDF(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CreatePDF2();
                        CreatePDF3();
                        CreatePDF4();
                        CreatePDF5();
                        CreatePDF6();
                                Toast.makeText(AdmDashboardActivity.this, "PDF Ready...", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
//                                startActivity(new Intent(AdmDashboardActivity.this, AdmLaporanActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        Toast.makeText(AdmDashboardActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(AdmDashboardActivity.this);
        requestQueue.add(request);
    }


    private void CreatePDF2(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 2", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdmDashboardActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(AdmDashboardActivity.this);
        requestQueue.add(request);
    }

    private void CreatePDF3(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 3", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdmDashboardActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(AdmDashboardActivity.this);
        requestQueue.add(request);
    }

    private void CreatePDF4(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 4", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdmDashboardActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(AdmDashboardActivity.this);
        requestQueue.add(request);
    }

    private void CreatePDF5(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 5", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdmDashboardActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(AdmDashboardActivity.this);
        requestQueue.add(request);
    }

    private void CreatePDF6(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 6", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdmDashboardActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(AdmDashboardActivity.this);
        requestQueue.add(request);
    }


    private void logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
        editor.apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}