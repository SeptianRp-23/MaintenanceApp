package com.azis.skripsiproject.Admin.Perbaikan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.azis.skripsiproject.Controller.Perbaikan.AdapterPengajuanProses;
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

public class AdmPerbaikanFamum extends AppCompatActivity {

    SessionManager sessionManager;
    String getId;
    ListView myList;
    AdapterPengajuanProses adapterPengajuanProses;
    public static ArrayList<DataItemPengajuan> dataItemPengajuanArrayList = new ArrayList<>();
    private String ShowBarang = Api.URL_API + "getPengajuanFamum.php";
    DataItemPengajuan dataItemPengajuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_perbaikan_famum);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        myList = findViewById(R.id.listfamum);
        adapterPengajuanProses = new AdapterPengajuanProses(this, dataItemPengajuanArrayList);
        myList.setAdapter(adapterPengajuanProses);

        final RelativeLayout relativeLayout = findViewById(R.id.relbmn);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdmPerbaikanFamum.this, AdmPerbaikanBmnActivity.class));
                overridePendingTransition(0,0);
            }
        });

        receiveData();

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                startActivity(new Intent(getApplicationContext(), AdmDetailPerbaikanFamum.class)
                        .putExtra("position", position));
            }
        });
    }

    public void receiveData(){
        final TextView txtKosong=findViewById(R.id.txt_famum_kosong);
        StringRequest request = new StringRequest(Request.Method.POST, ShowBarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataItemPengajuanArrayList.clear();
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

                                    txtKosong.setVisibility(View.GONE);
                                    dataItemPengajuan = new DataItemPengajuan(id, id_user, nama_user, id_barang, jenis, tipe, nama, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar, status);
                                    dataItemPengajuanArrayList.add(dataItemPengajuan);
                                    adapterPengajuanProses.notifyDataSetChanged();
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
                        Toast.makeText(AdmPerbaikanFamum.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(AdmPerbaikanFamum.this, AdmDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}