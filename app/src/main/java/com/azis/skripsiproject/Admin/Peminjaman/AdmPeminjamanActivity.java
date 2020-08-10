package com.azis.skripsiproject.Admin.Peminjaman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.azis.skripsiproject.Admin.Dashboard.AdmDashboardActivity;
import com.azis.skripsiproject.Controller.Peminjaman.AdapterPeminjaman;
import com.azis.skripsiproject.Controller.Peminjaman.DataItemPeminjaman;
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
    TextView txtKembali, txtReady;
    CardView cardTambah;
    AdapterPeminjaman adapterPeminjaman;
    ImageView btBack;
    public static ArrayList<DataItemPeminjaman> dataItemPeminjamanArrayList = new ArrayList<>();
    private String ShowBarang = Api.URL_API + "getDataPeminjaman.php";
    private String SetKembali = Api.URL_API + "updatePengembalian.php";
    private String SetBarang = Api.URL_API + "updateBarangBMN.php";
    DataItemPeminjaman dataItemPeminjaman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_peminjaman);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        receiveData();

        myList = findViewById(R.id.list_peminjaman);
        adapterPeminjaman = new AdapterPeminjaman(this, dataItemPeminjamanArrayList);
        myList.setAdapter(adapterPeminjaman);
        btBack = findViewById(R.id.back);
        cardTambah = findViewById(R.id.card_add);
        txtKembali = findViewById(R.id.txtKembali);
        txtReady = findViewById(R.id.txtReady);

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

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Loading. . .");
                final CharSequence[] dialogItem = {"Peminjaman Selesai"};
                builder.setTitle(dataItemPeminjamanArrayList.get(position).getPenggunaPmjn());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                final String id = dataItemPeminjamanArrayList.get(position).getIdPmjn();
                                final String kembali = txtKembali.getText().toString().trim();

                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, SetKembali,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String success = jsonObject.getString("success");
                                                    if (success.equals("1")){
//                                                        Toast.makeText(AdmPeminjamanActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                                        receiveData();
                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, SetBarang,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            String success = jsonObject.getString("success");

                                                                            if (success.equals("1")){
                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(AdmPeminjamanActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                                                                receiveData();
                                                                                Intent intent = new Intent(AdmPeminjamanActivity.this, AdmDashboardActivity.class);
                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                startActivity(intent);
                                                                                System.out.println("Berhasil");
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            System.out.println(e.toString());
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(AdmPeminjamanActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        System.out.println(error.toString());
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(AdmPeminjamanActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                        {
                                                            @Override
                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                Map<String, String> params = new HashMap<>();
                                                                params.put("status", txtReady.getText().toString());
                                                                params.put("no_inventaris", dataItemPeminjamanArrayList.get(position).getNo_inventarisPmjn());
                                                                return params;
                                                            }
                                                        };
                                                        RequestQueue requestQueue = Volley.newRequestQueue(AdmPeminjamanActivity.this);
                                                        requestQueue.add(stringRequest);
                                                    }
                                                } catch (JSONException e) {
                                                    System.out.println(e.toString());
                                                    Toast.makeText(AdmPeminjamanActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println(error.toString());
                                                Toast.makeText(AdmPeminjamanActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("status", kembali);
                                        params.put("id", id);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(AdmPeminjamanActivity.this);
                                requestQueue.add(stringRequest);
                                receiveData();
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

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