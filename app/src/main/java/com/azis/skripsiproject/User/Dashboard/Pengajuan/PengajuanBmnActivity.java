package com.azis.skripsiproject.User.Dashboard.Pengajuan;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Admin.Perbaikan.AdmDataPerbaikanActivity;
import com.azis.skripsiproject.Admin.Perbaikan.AdmDetailPerbaikan;
import com.azis.skripsiproject.Controller.ApiInterface;
import com.azis.skripsiproject.Controller.Data;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.Server.ApiClient;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengajuanBmnActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 7;
    Uri imageUri;
    int position;
    EditText etIdUser, etNama, etidBrg, etjenis, etTipe, etPengguna, etPokja, etKerusakan, etUraian, etTanggal, etkomponen, etbiaya, etStatus;
    ImageView imgbawah1, imgatas1, imgbawah2, imgatas2, imgbawah3, imgatas3, imgfoto;
    Button btChose, btKirim;
    TextView textRusak;
    LinearLayout lin1, lin2, lin3;
    private String updateBarang = Api.URL_API + "updatePeminjaman.php";
    String myFormat = "dd-MM-yyy hh:mm a";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private Bitmap bitmap;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    String getId, getNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_bmn);

        etIdUser = findViewById(R.id.et_idUser);
        etNama = findViewById(R.id.et_nama);
        etidBrg = findViewById(R.id.et_noBmn);
        etjenis = findViewById(R.id.et_jenis);
        etTipe = findViewById(R.id.et_tipe);
        etPengguna = findViewById(R.id.et_pengguna);
        etPokja = findViewById(R.id.et_pokja);
        etKerusakan = findViewById(R.id.et_kerusakan);
        etUraian = findViewById(R.id.et_uraian);
        etTanggal = findViewById(R.id.et_tanggal);
        etkomponen = findViewById(R.id.komponen);
        etbiaya = findViewById(R.id.biaya);
        etStatus = findViewById(R.id.status);
        btChose = findViewById(R.id.btn_chose);
        imgfoto = findViewById(R.id.img);
        btKirim = findViewById(R.id.btn_kirim);
        textRusak = findViewById(R.id.txtRusak);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getNama = user.get(SessionManager.NAME);

        etIdUser.setText(getId);
        etNama.setText(getNama);

        //attribut 1
        imgatas1 = findViewById(R.id.show1_2);
        imgbawah1 = findViewById(R.id.show1);
        lin1 = findViewById(R.id.linear1);

        imgbawah1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin1.setVisibility(View.VISIBLE);
                imgatas1.setVisibility(View.VISIBLE);
                imgbawah1.setVisibility(View.GONE);
            }
        });

        imgatas1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin1.setVisibility(View.GONE);
                imgatas1.setVisibility(View.GONE);
                imgbawah1.setVisibility(View.VISIBLE);
            }
        });


//        attribut 2
        lin2 = findViewById(R.id.linear2);
        imgatas2 = findViewById(R.id.show2_2);
        imgbawah2 = findViewById(R.id.show2);

        imgbawah2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin2.setVisibility(View.VISIBLE);
                imgatas2.setVisibility(View.VISIBLE);
                imgbawah2.setVisibility(View.GONE);
            }
        });

        imgatas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin2.setVisibility(View.GONE);
                imgatas2.setVisibility(View.GONE);
                imgbawah2.setVisibility(View.VISIBLE);
            }
        });

//        attribut 3
        lin3 = findViewById(R.id.linear3);
        imgbawah3 = findViewById(R.id.show3);
        imgatas3 = findViewById(R.id.show3_2);

        imgbawah3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin3.setVisibility(View.VISIBLE);
                imgatas3.setVisibility(View.VISIBLE);
                imgbawah3.setVisibility(View.GONE);
            }
        });

        imgatas3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin3.setVisibility(View.GONE);
                imgatas3.setVisibility(View.GONE);
                imgbawah3.setVisibility(View.VISIBLE);
            }
        });

        btChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        btKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etbiaya.getText().toString()) ||
                        TextUtils.isEmpty(etidBrg.getText().toString()) ||
                        TextUtils.isEmpty(etIdUser.getText().toString()) ||
                        TextUtils.isEmpty(etKerusakan.getText().toString()) ||
                        TextUtils.isEmpty(etkomponen.getText().toString()) ||
                        TextUtils.isEmpty(etPengguna.getText().toString()) ||
                        TextUtils.isEmpty(etPokja.getText().toString()) ||
                        TextUtils.isEmpty(etTipe.getText().toString()) ||
                        TextUtils.isEmpty(etUraian.getText().toString()) ||
                        TextUtils.isEmpty(etjenis.getText().toString())) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PengajuanBmnActivity.this);
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
        etidBrg.setText(PilihPenggunaActivity.dataItemAdminArrayList.get(position).getId());
        etjenis.setText(PilihPenggunaActivity.dataItemAdminArrayList.get(position).getJenis());
        etTipe.setText(PilihPenggunaActivity.dataItemAdminArrayList.get(position).getTipe());
        etPengguna.setText(PilihPenggunaActivity.dataItemAdminArrayList.get(position).getPengguna());
        etPokja.setText(PilihPenggunaActivity.dataItemAdminArrayList.get(position).getPokja());

        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        etTanggal.setText(str1);
        etTanggal.setEnabled(false);
    }

    private void postData(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

//        readMode();

        String idUser = etIdUser.getText().toString().trim();
        String namaUser = etNama.getText().toString().trim();
        String idBrg = etidBrg.getText().toString().trim();
        String jenis = etjenis.getText().toString().trim();
        String tipe = etTipe.getText().toString().trim();
        String pengguna = etPengguna.getText().toString().trim();
        String pokja = etPokja.getText().toString().trim();
        String kerusakan = etKerusakan.getText().toString().trim();
        String uraian = etUraian.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String keterangan = etkomponen.getText().toString().trim();
        String biaya = etbiaya.getText().toString().trim();
        String gambar = null;
        String status = etStatus.getText().toString().trim();
        if (bitmap == null) {
            gambar = "";
        } else {
            gambar = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Data> call = apiInterface.insertData(key, idUser, namaUser, idBrg, jenis, tipe, pengguna, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar, status);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                progressDialog.dismiss();

                Log.i(PengajuanBmnActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(PengajuanBmnActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    SaveEditDetail();
                    startActivity(new Intent(PengajuanBmnActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(PengajuanBmnActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PengajuanBmnActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

    private void SaveEditDetail() {

        final String status = this.textRusak.getText().toString().trim();
        final String id = this.etidBrg.getText().toString().trim();

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
//                                Toast.makeText(PengajuanBmnActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(PengajuanBmnActivity.this, AdmDataPerbaikanActivity.class));
//                                sessionManager.createSession(email, name, id);
                                System.out.println("Berhasil");
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(PengajuanBmnActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(PengajuanBmnActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
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
