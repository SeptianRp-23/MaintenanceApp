package com.azis.skripsiproject.User.Dashboard.Pengajuan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.azis.skripsiproject.Controller.ApiInterface;
import com.azis.skripsiproject.Controller.ApiInterface2;
import com.azis.skripsiproject.Controller.Data;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.ApiClient;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengajuanFamumActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 7;
    String myFormat = "EEEE, dd MMMM yyyy";
    Uri imageUri;
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    MaterialEditText etIdUser, etNama, etidBrg, etTipe, etPengguna, etPokja, etUraian, etTanggal, etkomponen, etbiaya;
    EditText etStatus;
    ImageView imgbawah1, imgatas1, imgbawah2, imgatas2, imgbawah3, imgatas3, imgfoto;
    Button btChose, btKirim;
    LinearLayout lin1, lin2, lin3;
    Spinner List;
    private RadioGroup opsi;
    String rgSelect="";

    private Bitmap bitmap;
    ApiInterface2 apiInterface2;
    SessionManager sessionManager;
    String getId, getNama;
    private Toast backToast;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_famum);

        etIdUser = findViewById(R.id.et_idUser);
        etNama = findViewById(R.id.et_nama);
        etidBrg = findViewById(R.id.et_noBmn);
        etTipe = findViewById(R.id.et_tipe);
        etPengguna = findViewById(R.id.et_pengguna);
        etPokja = findViewById(R.id.et_pokja);
        etUraian = findViewById(R.id.et_uraian);
        etTanggal = findViewById(R.id.et_tanggal);
        etkomponen = findViewById(R.id.komponen);
        etbiaya = findViewById(R.id.biaya);
        etStatus = findViewById(R.id.status);
        btChose = findViewById(R.id.btn_chose);
        imgfoto = findViewById(R.id.img);
        btKirim = findViewById(R.id.btn_kirim);
        List = findViewById(R.id.listItem);
        opsi = findViewById(R.id.rg1);

        opsi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.rb_rusak_ringan:
                        rgSelect = "Rusak Ringan";
                        Toast.makeText(PengajuanFamumActivity.this, ""+rgSelect, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_rusak_berat:
                        rgSelect = "Rusak Berat";
                        Toast.makeText(PengajuanFamumActivity.this, ""+rgSelect, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getNama = user.get(SessionManager.NAME);

        etIdUser.setText(getId);
        etNama.setText(getNama);
        etIdUser.setEnabled(false);
        etNama.setEnabled(false);

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

        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        etTanggal.setText(str1);
        etTanggal.setEnabled(false);
        //end

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
                        TextUtils.isEmpty(etkomponen.getText().toString()) ||
                        TextUtils.isEmpty(etPengguna.getText().toString()) ||
                        TextUtils.isEmpty(etPokja.getText().toString()) ||
                        TextUtils.isEmpty(etTipe.getText().toString()) ||
                        TextUtils.isEmpty(etUraian.getText().toString())){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PengajuanFamumActivity.this);
                    alertDialog.setMessage("field Kosong");
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
    }

    private void postData(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

//        readMode();

        String idUser = etIdUser.getText().toString().trim();
        String namaUser = etNama.getText().toString().trim();
        String idBrg = etidBrg.getText().toString().trim();
        String jenis = List.getSelectedItem().toString().trim();
        String tipe = etTipe.getText().toString().trim();
        String pengguna = etPengguna.getText().toString().trim();
        String pokja = etPokja.getText().toString().trim();
        String kerusakan = rgSelect;
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

        apiInterface2 = ApiClient.getApiClient().create(ApiInterface2.class);

        Call<Data> call = apiInterface2.insertData(key, idUser, namaUser, idBrg, jenis, tipe, pengguna, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar, status);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                progressDialog.dismiss();

                Log.i(PengajuanBmnActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(PengajuanFamumActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PengajuanFamumActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(PengajuanFamumActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PengajuanFamumActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

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

}