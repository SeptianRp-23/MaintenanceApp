package com.azis.skripsiproject.User.Proses.DetailsPengajuan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Controller.Perbaikan.DataItemPengajuan;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.Pengajuan.PilihBarangActivity;
import com.azis.skripsiproject.User.Proses.ProsesStatusActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProsesStatusDetail extends AppCompatActivity {

    private static final String TAG = ProsesStatusDetail.class.getSimpleName() ;
    MaterialEditText etIdPengajuan, etTanggal, etIdBarang, etJenis, etTipe, etNamaPengguna, etDivisi;
    TextView tvKeterangan, ket;
    LinearLayout tvDiTolak;
    Button btProses;
    int position;
    SessionManager sessionManager;
    ImageView btBack;
    String getId, getInv;
    private String getPerbaikan = Api.URL_API + "getPengajuanUser.php";

//    private String GetUserAPI = Api.URL_API + "getPengajuanUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_status_detail);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        etIdPengajuan = findViewById(R.id.et_id);
        etTanggal = findViewById(R.id.et_tanggal_pengajuan);
        etIdBarang = findViewById(R.id.et_id_barang);
        etJenis = findViewById(R.id.et_jenis_brg);
        etTipe = findViewById(R.id.et_tipe_brg);
        etNamaPengguna = findViewById(R.id.et_nama_pengguna);
        etDivisi = findViewById(R.id.et_divisi);
        tvKeterangan = findViewById(R.id.txt_ket_pengajuan);
        tvDiTolak = findViewById(R.id.txt_ditolak);
        btProses = findViewById(R.id.btn_proses);
        btBack = findViewById(R.id.back);
//        ket = findViewById(R.id.ket);

//        receiveData();
//        getUserDetail();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProsesStatusDetail.this, ProsesStatusActivity.class));
            }
        });

        //SetData
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        etIdPengajuan.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getId());
        etTanggal.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getTanggal());
        etIdBarang.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getId_barang());
        etJenis.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getJenis());
        etTipe.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getTipe());
        etNamaPengguna.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getNama());
        etDivisi.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getPokja());
        tvKeterangan.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getStatus());

        String status = tvKeterangan.getText().toString();
        if (status == "Di Tolak"){
            tvDiTolak.setVisibility(View.VISIBLE);
        }
        if (status == "Di Setujui"){
//            tvDiTolak.setVisibility(View.GONE);
            btProses.setVisibility(View.VISIBLE);
        }
    }
    private void receiveData(){
        getInv = etIdPengajuan.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, getPerbaikan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String status = object.getString("status");
                                    Toast.makeText(ProsesStatusDetail.this, "status"+status, Toast.LENGTH_SHORT).show();
                                    if (status.equals("Di Tolak")){
                                        tvDiTolak.setVisibility(View.VISIBLE);
                                    }
                                    else if (status.equals("Di Setujui")){
                                        btProses.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                        catch (JSONException e){
                            e.toString();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProsesStatusDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getInv);
//                params.put("id_user", txtIDuser);
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
}