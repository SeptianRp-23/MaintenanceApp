package com.azis.skripsiproject.User.Proses.DetailsPengajuan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Controller.ApiInterface;
import com.azis.skripsiproject.Controller.ApiInterfaceLapor;
import com.azis.skripsiproject.Controller.Data;
import com.azis.skripsiproject.Controller.DataLapor;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.Server.ApiClient;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;
import com.azis.skripsiproject.User.Dashboard.Pengajuan.PengajuanBmnActivity;
import com.azis.skripsiproject.User.Dashboard.Pengajuan.PilihPenggunaActivity;
import com.azis.skripsiproject.User.Proses.ProsesStatusActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProsesLaporSelesai extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 7;
    Uri imageUri;
    int position;
    MaterialEditText etID, etJenis, etItem, jenisKerusakan, detailKerusakan, etPekerjaan, etBiaya, etTangggal, etTgl;
    ImageView imgfoto;
    Button btChose, btKirim;
    private String updateBarang = Api.URL_API + "updatePeminjaman.php";
    String myFormat = "EEEE, dd MMMM yyyy";
    private String updateProgres = Api.URL_API + "updateProgres.php";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    private Bitmap bitmap;
    ApiInterfaceLapor apiInterfaceLapor;
    SessionManager sessionManager;
    String getId, getNama, getIdBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_lapor_selesai);

        etID = findViewById(R.id.selesai_et_id_);
        etJenis = findViewById(R.id.selesai_et_jenis_brg);
        etItem = findViewById(R.id.selesai_et_tipe_brg);
        jenisKerusakan = findViewById(R.id.selesai_et_kerusakan);
        detailKerusakan = findViewById(R.id.selesai_et_detail_kerusakan);
        etPekerjaan = findViewById(R.id.selesai_et_pekerjaan);
        etBiaya = findViewById(R.id.selesai_et_biaya_pekerjaan);
        etTgl = findViewById(R.id.selesai_et_tanggal_pengajuan);
        etTangggal = findViewById(R.id.et_tanggal_selesai);
        btChose = findViewById(R.id.btn_chosee);
        imgfoto = findViewById(R.id.img);
        btKirim = findViewById(R.id.btn_proses);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getNama = user.get(SessionManager.NAME);

        btChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
                btKirim.setVisibility(View.VISIBLE);
            }
        });

        btKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etID.getText().toString()) ||
                        TextUtils.isEmpty(etJenis.getText().toString()) ||
                        TextUtils.isEmpty(etItem.getText().toString()) ||
                        TextUtils.isEmpty(jenisKerusakan.getText().toString()) ||
                        TextUtils.isEmpty(detailKerusakan.getText().toString()) ||
                        TextUtils.isEmpty(etPekerjaan.getText().toString()) ||
                        TextUtils.isEmpty(etBiaya.getText().toString()) ||
                        TextUtils.isEmpty(etTgl.getText().toString()) ||
                        TextUtils.isEmpty(etTangggal.getText().toString())) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProsesLaporSelesai.this);
                    alertDialog.setMessage("Please complete the field!");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    postData("insert");
                }
            }
        });

        //SetData
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        etID.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getId());
        etTgl.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getTanggal());
        etJenis.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getJenis());
        etItem.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getTipe());
        jenisKerusakan.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getKerusakan());
        detailKerusakan.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getUraian());
        etPekerjaan.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getKeterangan());
        etBiaya.setText(ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getBiaya());
        getIdBarang = (ProsesStatusActivity.dataItemPengajuanArrayList.get(position).getId_barang());

        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        etTangggal.setText(str1);
    }

    private void postData(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

//        readMode();

        String idPerbaikan = etID.getText().toString().trim();
        String idUser = getId;
        String nama = getNama;
        String jenis = etJenis.getText().toString();
        String tipe = etItem.getText().toString().trim();
        String kerusakan = jenisKerusakan.getText().toString().trim();
        String detail = detailKerusakan.getText().toString().trim();
        String perbaikan = etPekerjaan.getText().toString().trim();
        String biaya = etBiaya.getText().toString().trim();
        String tanggal = etTangggal.getText().toString();
        String gambar = null;
        if (bitmap == null) {
            gambar = "";
        } else {
            gambar = getStringImage(bitmap);
        }

        apiInterfaceLapor = ApiClient.getApiClient().create(ApiInterfaceLapor.class);

        Call<DataLapor> call = apiInterfaceLapor.insertData(key, idPerbaikan, idUser, nama, jenis, tipe, kerusakan, detail, perbaikan, biaya, tanggal, gambar);

        call.enqueue(new Callback<DataLapor>() {
            @Override
            public void onResponse(Call<DataLapor> call, Response<DataLapor> response) {

                progressDialog.dismiss();

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(ProsesLaporSelesai.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    UpdatePerbaikan();
                    UpdatePemakaian();
                    startActivity(new Intent(ProsesLaporSelesai.this, ProsesStatusActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProsesLaporSelesai.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DataLapor> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProsesLaporSelesai.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseFile() {

        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);

//        CharSequence[] item = {"Kamera", "Galeri"};
//        AlertDialog.Builder request = new AlertDialog.Builder(this)
//                .setTitle("Add Image")
//                .setItems(item, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i){
//                            case 0:
//                                //Membuka Kamera Untuk Mengambil Gambar
//                                EasyImage.openCamera(PengajuanBmnActivity.this, REQUEST_CODE_CAMERA);
////                                        Intent intent = new Intent();
////                                        intent.setType("image/*");
////                                        intent.setAction(Intent.ACTION_GET_CONTENT);
////                                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//                                break;
//                            case 1:
////                                //Membuaka Galeri Untuk Mengambil Gambar
//                                EasyImage.openGallery(PengajuanBmnActivity.this, REQUEST_CODE_GALLERY);
//                                break;
//                        }
//                    }
//                });
//        request.create();
//        request.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
//        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//
//                imgfoto.setImageBitmap(bitmap);
        if (resultCode == Activity.RESULT_OK)
            try {
                if (bitmap != null) {
                    bitmap.recycle();
                }

                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(imageUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
//                imageUriString = cursor.getString(column_index);

                getContentResolver().notifyChange(imageUri, null);
                ContentResolver cr = getContentResolver();

                try {
                    bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
                    imgfoto.setImageBitmap(bitmap);

                } catch (Exception e) {

                }
            } catch (Exception e) {

            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void UpdatePemakaian() {

        final String status = "Di Gunakan";
        final String id = getIdBarang;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateBarang,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                System.out.println("Berhasil");
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(ProsesLaporSelesai.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(ProsesLaporSelesai.this, "Error Server", Toast.LENGTH_SHORT).show();
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

    private void UpdatePerbaikan() {
        final String status = "Selesai";
        final String id = this.etID.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateProgres,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(ProsesLaporSelesai.this, "Success!", Toast.LENGTH_SHORT).show();
//                                sessionManager.createSession(email, name, id);
                                System.out.println("Berhasil");
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
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
}