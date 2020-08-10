package com.azis.skripsiproject.User.Proses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.azis.skripsiproject.Controller.Perbaikan.AdapterPengajuanProses;
import com.azis.skripsiproject.Controller.Perbaikan.AdapterPengajuanUser;
import com.azis.skripsiproject.Controller.Perbaikan.DataItemPengajuan;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;
import com.azis.skripsiproject.User.Dashboard.User.ProfileActivity;
import com.azis.skripsiproject.User.Laporan.LaporanActivity;
import com.azis.skripsiproject.User.Proses.DetailsPengajuan.ProsesLaporSelesai;
import com.azis.skripsiproject.User.Proses.DetailsPengajuan.ProsesStatusDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProsesStatusActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    SessionManager sessionManager;
    String getID;
    ListView myList;
    TextView setid, txtKosong;
//    Handler mHandler;
    AdapterPengajuanProses adapterPengajuanUser;
    public static ArrayList<DataItemPengajuan> dataItemPengajuanArrayList = new ArrayList<>();
    private String getPerbaikan = Api.URL_API + "getPengajuanProses.php";
    DataItemPengajuan dataItemPengajuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_status);

//        this.mHandler = new Handler();
//        this.mHandler.postDelayed(m_Runnable,2000);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);

        final LinearLayout btLapor = findViewById(R.id.lin_lapor);
        btLapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        myList = findViewById(R.id.list);
        adapterPengajuanUser = new AdapterPengajuanProses(this, dataItemPengajuanArrayList);
        myList.setAdapter(adapterPengajuanUser);


        setid = findViewById(R.id.txtgetid);
        setid.setText(getID);
        txtKosong = findViewById(R.id.txt_data_kosong);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                startActivity(new Intent(getApplicationContext(), ProsesLaporSelesai.class)
                        .putExtra("position", position));
            }
        });

        receiveData();

        //ButtomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.status);
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
//                        startActivity(new Intent(getApplicationContext(),
//                                StatusActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                    case R.id.account:
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

//    private final Runnable m_Runnable = new Runnable() {
//        public void run() {
//            ProsesStatusActivity.this.mHandler.postDelayed(m_Runnable, 5000);
//            receiveData();
//        }
//    };

    private void receiveData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat Data . . .");
//        loading.setVisibility(View.VISIBLE);
        progressDialog.show();
        final String etId = setid.getText().toString().trim();
        StringRequest request = new StringRequest(Request.Method.POST, getPerbaikan,
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
                                    txtKosong.setVisibility(View.GONE);
                                    dataItemPengajuan = new DataItemPengajuan(id, id_user, nama_user, id_barang, jenis, tipe, nama, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar, status);
                                    dataItemPengajuanArrayList.add(dataItemPengajuan);
                                    adapterPengajuanUser.notifyDataSetChanged();

                                }
                            }
                        }
                        catch (JSONException e){
                            e.toString();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProsesStatusActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("id_user", getID);
                params.put("id_user", etId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiveData();
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