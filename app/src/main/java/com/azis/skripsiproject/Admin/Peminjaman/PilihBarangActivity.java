package com.azis.skripsiproject.Admin.Peminjaman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.azis.skripsiproject.Controller.Peminjaman.AdapterBarang;
import com.azis.skripsiproject.Controller.Peminjaman.DataItemBarang;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PilihBarangActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String getId;
    ListView listPilih;
    AdapterBarang adapterBarang;
    public static ArrayList<DataItemBarang> dataItemBarangArrayList = new ArrayList<>();
    private String ShowBarang = Api.URL_API + "pilihBarang.php";
    DataItemBarang dataItemBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_barang2);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        listPilih = findViewById(R.id.myListviewPilih);
        adapterBarang = new AdapterBarang(PilihBarangActivity.this, dataItemBarangArrayList);
        listPilih.setAdapter(adapterBarang);

        listPilih.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                startActivity(new Intent(getApplicationContext(), AdmTambahPeminjaman.class)
                        .putExtra("position", position));

//                CharSequence[] dialogItem = {"View Data", "Edit Data", "Delete Data"};
//                builder.setTitle(dataItemAdminArrayList.get(position).getPengguna());
//                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        switch (i) {
//                            case 0:
//
//                                break;
//                            case 1:
//                                break;
//                            case 2:
//                                break;
//                        }
//                    }
//                });
//                builder.create().show();
            }
        });
        receiveData();
    }
    public void receiveData(){
        StringRequest request = new StringRequest(Request.Method.POST, ShowBarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataItemBarangArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String no_invesBrg = object.getString("no_inventaris");
                                    String jenisBrg = object.getString("jenis");
                                    String tipeBrg = object.getString("tipe");
                                    String tanggalBrg = object.getString("tanggal");
                                    String statusBrg = object.getString("status");

                                    dataItemBarang = new DataItemBarang(no_invesBrg, jenisBrg, tipeBrg, tanggalBrg, statusBrg);
                                    dataItemBarangArrayList.add(dataItemBarang);
                                    adapterBarang.notifyDataSetChanged();
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
                        Toast.makeText(PilihBarangActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(PilihBarangActivity.this, AdmDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}