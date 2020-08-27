package com.azis.skripsiproject.User.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Controller.Perbaikan.AdapterPengajuanUser;
import com.azis.skripsiproject.Controller.Perbaikan.DataItemPengajuan;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.Pengajuan.PilihPenggunaActivity;
import com.azis.skripsiproject.User.Dashboard.Pengajuan.PengajuanFamumActivity;
import com.azis.skripsiproject.User.Dashboard.User.ProfileActivity;
import com.azis.skripsiproject.User.Laporan.LaporanActivity;
import com.azis.skripsiproject.User.Proses.DetailsPengajuan.ProsesStatusDetail;
import com.azis.skripsiproject.User.Proses.ProsesStatusActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String getID;
    TextView txtBagian, txtId;
    CardView cBmn, cFamum;
    ListView myList;
    Handler mHandler;
    AdapterPengajuanUser adapterPengajuanUser;
    public static ArrayList<DataItemPengajuan> dataItemPengajuanArrayList = new ArrayList<>();
    private String getPerbaikanBmn = Api.URL_API + "getPengajuanbyUser.php";
    DataItemPengajuan dataItemPengajuan;
    TextView txtKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);

        txtBagian = findViewById(R.id.bagian);
        cBmn = findViewById(R.id.cardBmn);
        cFamum = findViewById(R.id.cardFamum);
        txtId = findViewById(R.id.getId);

        myList = findViewById(R.id.list);
        adapterPengajuanUser = new AdapterPengajuanUser(this, dataItemPengajuanArrayList);
        myList.setAdapter(adapterPengajuanUser);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                startActivity(new Intent(getApplicationContext(), ProsesStatusDetail.class)
                        .putExtra("position", position));
            }
        });

        final String mId = user.get(SessionManager.ID);
        txtId.setText(mId);

        //check user login bmn atau famum
        final String mbagian = user.get(SessionManager.BAGIAN);
        if (mbagian.equals("bmn")){
            txtBagian.setText("Perbaikan BMN");
        }
        else if (mbagian.equals("famum")){
            txtBagian.setText("Perbaikan Fasilitas Kantor");
        }

        final MediaPlayer mpbmn = MediaPlayer.create(this, R.raw.laporkerusakan);
        final String bagian = txtBagian.getText().toString().trim();
        if (bagian.equals("Perbaikan BMN")){
            cBmn.setVisibility(View.VISIBLE);
//            receiveDataBMN();
            receiveDataBMN();
        }
        else if (bagian.equals("Perbaikan Fasilitas Kantor")){
            cFamum.setVisibility(View.VISIBLE);
            receiveDataFAMUM();
//            receiveDataBMN();
        }
        //end check


        cBmn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, PilihPenggunaActivity.class));
                mpbmn.start();
            }
        });

        cFamum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, PengajuanFamumActivity.class));
                mpbmn.start();
            }
        });



        final MediaPlayer utama = MediaPlayer.create(this, R.raw.menuutama);
        final MediaPlayer proses = MediaPlayer.create(this, R.raw.menuproses);
        final MediaPlayer profile = MediaPlayer.create(this, R.raw.menuprofile);
        //ButtomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
//                        startActivity(new Intent(getApplicationContext(),
//                                TransaksiActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                    case R.id.status:
                        proses.start();
                        startActivity(new Intent(getApplicationContext(),
                                ProsesStatusActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id. account:
                        profile.start();
                        startActivity(new Intent(getApplicationContext(),
                                ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //End ButtomNav

    }

    public void receiveDataBMN(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat Data . . .");
//        loading.setVisibility(View.VISIBLE);
        progressDialog.show();
        final String txtIDuser = txtId.getText().toString().trim();
        StringRequest request = new StringRequest(Request.Method.POST, getPerbaikanBmn,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataItemPengajuanArrayList.clear();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String id_user = object.getString("id_user");
                                    String nama_user = object.getString("nama_user");
                                    String id_barang = object.getString("id_barang");
                                    String jenis = object.getString("jenis");
                                    String tipe = object.getString("tipe");
                                    String nama = object.getString("nama");
                                    String pokja = object.getString("pokja");
                                    String kerusakan = object.getString("kerusakan");
                                    String uraian = object.getString("uraian");
                                    String tanggal = object.getString("tanggal");
                                    String keterangan = object.getString("keterangan");
                                    String biaya = object.getString("biaya");
                                    String gambar = object.getString("gambar");
                                    String status = object.getString("status");

                                    progressDialog.dismiss();
                                        dataItemPengajuan = new DataItemPengajuan(id, id_user, nama_user, id_barang, jenis, tipe, nama, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar, status);
                                        dataItemPengajuanArrayList.add(dataItemPengajuan);
                                        adapterPengajuanUser.notifyDataSetChanged();
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getID);
//                params.put("id_user", txtIDuser);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void receiveDataFAMUM(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat Data . . .");
//        loading.setVisibility(View.VISIBLE);
        progressDialog.show();
        final String txtIDuser = txtId.getText().toString().trim();
        StringRequest request = new StringRequest(Request.Method.POST, getPerbaikanBmn,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataItemPengajuanArrayList.clear();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String id_user = object.getString("id_user");
                                    String nama_user = object.getString("nama_user");
                                    String id_barang = object.getString("id_barang");
                                    String jenis = object.getString("jenis");
                                    String tipe = object.getString("tipe");
                                    String nama = object.getString("nama");
                                    String pokja = object.getString("pokja");
                                    String kerusakan = object.getString("kerusakan");
                                    String uraian = object.getString("uraian");
                                    String tanggal = object.getString("tanggal");
                                    String keterangan = object.getString("keterangan");
                                    String biaya = object.getString("biaya");
                                    String gambar = object.getString("gambar");
                                    String status = object.getString("status");

                                    progressDialog.dismiss();
                                        dataItemPengajuan = new DataItemPengajuan(id, id_user, nama_user, id_barang, jenis, tipe, nama, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar, status);
                                        dataItemPengajuanArrayList.add(dataItemPengajuan);
                                        adapterPengajuanUser.notifyDataSetChanged();
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getID);
//                params.put("id_user", txtIDuser);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        receiveData();
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.account:
//                Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
//                startActivity(i);
//                return true;
//            case R.id.logout:
//                Toast.makeText(this, "Terimakasih", Toast.LENGTH_SHORT).show();
//                logout();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    public void user(View view) {
//        Intent intent = new Intent(MainActivity.this, TambahUserActivity.class);
//        startActivity(intent);
//    }
//    public void mobil(View view) {
//        Intent intent = new Intent(MainActivity.this, DataMobilActivity.class);
//        startActivity(intent);
//    }

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