package com.azis.skripsiproject.Admin.LaporPerbaikan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Admin.Perbaikan.AdmDetailPerbaikanFamum;
import com.azis.skripsiproject.Admin.Perbaikan.AdmPerbaikanBmnActivity;
import com.azis.skripsiproject.Controller.Perbaikan.DataItemPengajuan;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdmLaporanSelesaiDetail extends AppCompatActivity {

    private String ShowBarang = Api.URL_API + "getPekerjaanSelesai.php";
    String getID =  "";
    int position;
    Button showImage;
    ImageView image;
    ProgressBar contentLoadingProgressBar;
    MaterialEditText mtID, mtNama, mtJenis, mtItem, mtTanggal, mtBiaya, mtGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_laporan_selesai_detail);

        mtID = findViewById(R.id.dps_id);
        mtNama = findViewById(R.id.dps_nama);
        mtJenis = findViewById(R.id.dps_jenis);
        mtItem = findViewById(R.id.dps_item);
        mtTanggal = findViewById(R.id.dps_tanggal);
        mtBiaya = findViewById(R.id.dps_biaya);
        mtGambar = findViewById(R.id.dps_gambar);
        showImage =  findViewById(R.id.btn_show);
        image =  findViewById(R.id.gambar);
        contentLoadingProgressBar = findViewById(R.id.dps_progress);

        //SetData
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        getID = (AdmLaporanPerbaikan.dataItemPengajuanArrayList.get(position).getId());

        receiveData();

        showImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showImage.setVisibility(View.GONE);
                contentLoadingProgressBar.setVisibility(View.VISIBLE);
                final String URL = mtGambar.getText().toString().trim();
                String U_R_L = Api.URL_IMAGE+URL;
                image.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .timeout(5000)
                        .error(R.drawable.ic_error);
                // Menjalankan proses dengan metode AsyncTask
//                new DownloadImage().execute(URL);

                Glide.with(AdmLaporanSelesaiDetail.this)
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

    }

    public void receiveData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat Data . . .");
//        loading.setVisibility(View.VISIBLE);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, ShowBarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String id_perbaikan = object.getString("id_perbaikan");
                                    String id_user = object.getString("id_user");
                                    String nama = object.getString("nama");
                                    String jenis = object.getString("jenis");
                                    String tipe = object.getString("tipe");
                                    String kerusakan = object.getString("kerusakan");
                                    String detail = object.getString("detail");
                                    String perbaikan = object.getString("perbaikan");
                                    String biaya = object.getString("biaya");
                                    String tanggal = object.getString("tanggal");
                                    String gambar = object.getString("gambar");

                                    progressDialog.dismiss();

                                    mtID.setText(id_perbaikan);
                                    mtNama.setText(nama);
                                    mtJenis.setText(jenis);
                                    mtItem.setText(tipe);
                                    mtTanggal.setText(tanggal);
                                    mtBiaya.setText(biaya);
                                    mtGambar.setText(gambar);
                                }
                            }
                        }
                        catch (JSONException e){
                            progressDialog.dismiss();
                            Toast.makeText(AdmLaporanSelesaiDetail.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdmLaporanSelesaiDetail.this, AdmLaporanPerbaikan.class));
                            overridePendingTransition(0,0);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AdmLaporanSelesaiDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_perbaikan", getID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}