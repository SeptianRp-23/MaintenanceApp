package com.azis.skripsiproject.Admin.Peminjaman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.azis.skripsiproject.Admin.Perbaikan.AdmDataPerbaikanActivity;
import com.azis.skripsiproject.Admin.Perbaikan.AdmDetailPerbaikan;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.Pengajuan.PengajuanFamumActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdmTambahPeminjaman extends AppCompatActivity {

    MaterialEditText etNoInves, etJenis, etTipe, etPeminjam, etJabatan, etTanggal;
    Spinner spJabatan;
    Button btSimpan;
    ImageView btBack;
    SessionManager sessionManager;
    String myFormat = "dd-MM-yyy hh:mm a";
    int position;
    TextView txtStatus;
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private String InsertTugas = Api.URL_API + "insertPeminjaman.php";
    private String UpdateDataBarang = Api.URL_API + "updateBarangBMN.php";
//    private String AddPeminjaman = Api.URL_API + "insertPeminjaman.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_tambah_peminjaman);

        etNoInves = findViewById(R.id.no_inv);
        etJenis = findViewById(R.id.jenis_barang);
        etTipe = findViewById(R.id.tipe_barang);
        etPeminjam = findViewById(R.id.nama_peminjam);
        etJabatan = findViewById(R.id.jabatan);
        etTanggal = findViewById(R.id.tgl_peminjaman);
        spJabatan = findViewById(R.id.spinner_jabaran);
        btSimpan = findViewById(R.id.btn_submit);
        btBack = findViewById(R.id.back);
        txtStatus = findViewById(R.id.txtDigunakan);

//        String value = spJabatan.getSelectedItem().toString();
//        etJabatan.setText(value);
        spJabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                String value = String.valueOf(item);
                etJabatan.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdmTambahPeminjaman.this, AdmPeminjamanActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //SetData
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        etNoInves.setText(PilihBarangActivity.dataItemBarangArrayList.get(position).getNo_invesBrg());
        etJenis.setText(PilihBarangActivity.dataItemBarangArrayList.get(position).getJenisBrg());
        etTipe.setText(PilihBarangActivity.dataItemBarangArrayList.get(position).getTipeBrg());

        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        etTanggal.setText(str1);

        etNoInves.setEnabled(false);
        etTanggal.setEnabled(false);
        etJenis.setEnabled(false);
        etTipe.setEnabled(false);

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final String txtNama = etPeminjam.getText().toString();
//                final String txtJabatan = etJabatan.getText().toString();
                    InsertData();
//                    SaveEditDetail();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdmTambahPeminjaman.this, AdmPeminjamanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void InsertData() {
        final String txtInv = etNoInves.getText().toString().trim();
        final String txtJenis = etJenis.getText().toString().trim();
        final String txtTipe = etTipe.getText().toString().trim();
        final String txtTanggal = etTanggal.getText().toString().trim();
        final String txtNama = etPeminjam.getText().toString().trim();
        final String txtPokja = etJabatan.getText().toString().trim();
        final String txStatus = txtStatus.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(AdmTambahPeminjaman.this);
        progressDialog.setMessage("Loading . . .");

        if (txtNama.isEmpty() || txtPokja.isEmpty()){
            Toast.makeText(AdmTambahPeminjaman.this, "Data Belum Lengkap!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, InsertTugas,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Toast.makeText(AdmTambahPeminjaman.this, "Success", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(AdmTambahPeminjaman.this, AdmDashboardActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AdmTambahPeminjaman.this, "Gagal", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AdmTambahPeminjaman.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("no_inventaris", txtInv);
                    params.put("jenis", txtJenis);
                    params.put("tipe", txtTipe);
                    params.put("tanggal", txtTanggal);
                    params.put("pengguna", txtNama);
                    params.put("pokja", txtPokja);
                    params.put("status", txStatus);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AdmTambahPeminjaman.this);
            requestQueue.add(request);
        }
    }

    private void SaveEditDetail() {

        final String status = this.txtStatus.getText().toString().trim();
        final String id = this.etNoInves.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateDataBarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(AdmTambahPeminjaman.this, "Berhasil . . .", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(AdmDetailPerbaikan.this, AdmDataPerbaikanActivity.class));
//                                sessionManager.createSession(email, name, id);
                                System.out.println("Berhasil Update");
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(AdmTambahPeminjaman.this, "Koneksi Bermasalah!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(AdmTambahPeminjaman.this, "Koneksi Bermasalah!", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                params.put("no_inventaris", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}