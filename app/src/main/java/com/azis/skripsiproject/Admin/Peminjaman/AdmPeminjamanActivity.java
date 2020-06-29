package com.azis.skripsiproject.Admin.Peminjaman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Admin.Dashboard.AdmDashboardActivity;
import com.azis.skripsiproject.Admin.Perbaikan.AdmDataPerbaikanActivity;
import com.azis.skripsiproject.Admin.Perbaikan.AdmDetailPerbaikan;
import com.azis.skripsiproject.Controller.Peminjaman.AdapterPeminjaman;
import com.azis.skripsiproject.Controller.Peminjaman.DataItemPeminjaman;
import com.azis.skripsiproject.Controller.Perbaikan.AdapterPengajuan;
import com.azis.skripsiproject.Controller.Perbaikan.DataItemPengajuan;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdmPeminjamanActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String getId;
    ListView myList;
    CardView cardTambah;
    AdapterPeminjaman adapterPeminjaman;
    ImageView btBack;
    public static ArrayList<DataItemPeminjaman> dataItemPeminjamanArrayList = new ArrayList<>();
    private String ShowBarang = Api.URL_API + "getDataPeminjaman.php";
    DataItemPeminjaman dataItemPeminjaman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_peminjaman);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        myList = findViewById(R.id.list_peminjaman);
        adapterPeminjaman = new AdapterPeminjaman(this, dataItemPeminjamanArrayList);
        myList.setAdapter(adapterPeminjaman);
        btBack = findViewById(R.id.back);
        cardTambah = findViewById(R.id.card_add);

        cardTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdmPeminjamanActivity.this, PilihBarangActivity.class));
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdmPeminjamanActivity.this, AdmDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        receiveData();

//        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                startActivity(new Intent(getApplicationContext(), AdmDetailPerbaikan.class)
//                        .putExtra("position", position));
//            }
//        });

    }

    public void receiveData(){
        StringRequest request = new StringRequest(Request.Method.POST, ShowBarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataItemPeminjamanArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String no_inv = object.getString("no_inventaris");
                                    String jenis = object.getString("jenis");
                                    String tipe = object.getString("tipe");
                                    String tanggal = object.getString("tanggal");
                                    String pengguna = object.getString("pengguna");
                                    String pokja = object.getString("pokja");
                                    String status = object.getString("status");

                                    dataItemPeminjaman = new DataItemPeminjaman(id, no_inv, jenis, tipe, tanggal, pengguna, pokja, status);                         dataItemPeminjamanArrayList.add(dataItemPeminjaman);
                                    adapterPeminjaman.notifyDataSetChanged();
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdmPeminjamanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdmPeminjamanActivity.this, AdmDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}