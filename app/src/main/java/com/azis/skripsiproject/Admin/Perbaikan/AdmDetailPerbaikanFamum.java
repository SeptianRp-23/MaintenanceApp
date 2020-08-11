package com.azis.skripsiproject.Admin.Perbaikan;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdmDetailPerbaikanFamum extends AppCompatActivity {

    ImageView image, btBack;
    Button btShow, btTolak, btTerima;
    TextView status;
    int position;
    ProgressBar contentLoadingProgressBar;
    private String updateProgres = Api.URL_API + "updateProgres.php";
    private MaterialEditText noPengajuan, namaPemohon, jenisFasilitas, itemBarang, tanggal, harga, linkGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_famum_detail_perbaikan);

        image       = findViewById(R.id.fam_gambar);
        noPengajuan = findViewById(R.id.fam_no_pengajuan);
        namaPemohon = findViewById(R.id.fam_nama_pemohon);
        jenisFasilitas = findViewById(R.id.fam_jenis_fasilitas);
        itemBarang  = findViewById(R.id.fam_item_perbaikan);
        tanggal     = findViewById(R.id.fam_tgl_pengajuan);
        harga       = findViewById(R.id.fam_estimasi_harga);
        btShow     = findViewById(R.id.fam_btn_lihat);
        btTerima   = findViewById(R.id.fam_btn_terima);
        btTolak    = findViewById(R.id.fam_btn_tolak);
        linkGambar    = findViewById(R.id.fam_no_gambar);
        status      = findViewById(R.id.fam_txt_keterangan);
        btBack      = findViewById(R.id.fam_back);
        contentLoadingProgressBar = findViewById(R.id.fam_progress);

        //SetData
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        noPengajuan.setText(AdmPerbaikanFamum.dataItemPengajuanArrayList.get(position).getId());
        namaPemohon.setText(AdmPerbaikanFamum.dataItemPengajuanArrayList.get(position).getNama_user());
        jenisFasilitas.setText(AdmPerbaikanFamum.dataItemPengajuanArrayList.get(position).getJenis());
        itemBarang.setText(AdmPerbaikanFamum.dataItemPengajuanArrayList.get(position).getTipe());
        tanggal.setText(AdmPerbaikanFamum.dataItemPengajuanArrayList.get(position).getTanggal());
        harga.setText(AdmPerbaikanFamum.dataItemPengajuanArrayList.get(position).getBiaya());
        linkGambar.setText(AdmPerbaikanFamum.dataItemPengajuanArrayList.get(position).getGambar());

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdmDetailPerbaikanFamum.this, AdmPerbaikanFamum.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        final LinearLayout btlayout = findViewById(R.id.fam_linbt);
        btShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                btShow.setVisibility(View.GONE);
                contentLoadingProgressBar.setVisibility(View.VISIBLE);
                final String URL = linkGambar.getText().toString().trim();
                String U_R_L = Api.URL_IMAGE+URL;
                btlayout.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .timeout(5000)
                        .error(R.drawable.ic_error);
                // Menjalankan proses dengan metode AsyncTask
//                new DownloadImage().execute(URL);

                Glide.with(AdmDetailPerbaikanFamum.this)
                        .load(U_R_L)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                contentLoadingProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                contentLoadingProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(requestOptions)
                        .into(image);
            }
        });

        btTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("Di Tolak");
                String getStatus = status.getText().toString().trim();
                if (getStatus == ""){
                    Toast.makeText(AdmDetailPerbaikanFamum.this, "kosong", Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(AdmDetailPerbaikanBmn.this, "benar "+getStatus, Toast.LENGTH_SHORT).show();
                    SaveEditDetail();
                }
            }
        });

        btTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("Di Setujui");
                String getStatus = status.getText().toString().trim();
                if (getStatus == ""){
                    Toast.makeText(AdmDetailPerbaikanFamum.this, "Error", Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(AdmDetailPerbaikanBmn.this, "bener "+getStatus, Toast.LENGTH_SHORT).show();
                    SaveEditDetail();
                }
            }
        });

    }

    private void SaveEditDetail() {

        final String status = this.status.getText().toString().trim();
        final String id = this.noPengajuan.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateProgres,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(AdmDetailPerbaikanFamum.this, "Success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdmDetailPerbaikanFamum.this, AdmPerbaikanFamum.class));
//                                sessionManager.createSession(email, name, id);
                                System.out.println("Berhasil");
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(AdmDetailPerbaikanFamum.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(AdmDetailPerbaikanFamum.this, "Error Server", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdmDetailPerbaikanFamum.this, AdmPerbaikanFamum.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}