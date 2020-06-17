package com.azis.skripsiproject.User.Dashboard.Pengajuan;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.azis.skripsiproject.Controller.ApiInterface;
import com.azis.skripsiproject.Controller.Data;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.ApiClient;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengajuanBmnActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_GALLERY = 002;
    String myFormat = "dd-MM-yyy hh:mm a";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private static final String TAG = "PengajuanBmnActivity";
    int position;
    EditText etIdUser, etNama, etidBrg, etjenis, etTipe, etPengguna, etPokja, etKerusakan, etUraian, etTanggal, etkomponen, etbiaya;
    ImageView imgbawah1, imgatas1, imgbawah2, imgatas2, imgbawah3, imgatas3, imgfoto;
    Button btChose, btKirim;
    LinearLayout lin1, lin2, lin3;

    private Bitmap bitmap;
    private CircleImageView mPicture;
    private FloatingActionButton mFabChoosePic;
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
        btChose = findViewById(R.id.btn_chose);
        imgfoto = findViewById(R.id.img);
        btKirim = findViewById(R.id.btn_kirim);

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
        etidBrg.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getNo_inventaris());
        etjenis.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getJenis());
        etTipe.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getTipe());
        etPengguna.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getPengguna());
        etPokja.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getPokja());
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
        if (bitmap == null) {
            gambar = "";
        } else {
            gambar = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Data> call = apiInterface.insertData(key, idUser, namaUser, idBrg, jenis, tipe, pengguna, pokja, kerusakan, uraian, tanggal, keterangan, biaya, gambar);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                progressDialog.dismiss();

                Log.i(PengajuanBmnActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(PengajuanBmnActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
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
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

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
//                                //        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//                                break;
//                            case 1:
//                                //Membuaka Galeri Untuk Mengambil Gambar
//                                EasyImage.openGallery(PengajuanBmnActivity.this, REQUEST_CODE_GALLERY);
//                                break;
//                        }
//                    }
//                });
//        request.create();
//        request.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imgfoto.setImageBitmap(bitmap);

//            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
//
//                @Override
//                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
//                    //Method Ini Digunakan Untuk Menghandle Error pada Image
//                }
//
//                @Override
//                public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
//                    //Method Ini Digunakan Untuk Menghandle Image
//                    switch (type){
//                        case REQUEST_CODE_CAMERA:
//                            Glide.with(PengajuanBmnActivity.this)
//                                    .load(imageFile)
//                                    .centerCrop()
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                    .into(imgfoto);
//                            break;
//
//                        case REQUEST_CODE_GALLERY:
//                            Glide.with(PengajuanBmnActivity.this)
//                                    .load(imageFile)
//                                    .centerCrop()
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                    .into(imgfoto);
//                            break;
//                    }
//                }
//
//                @Override
//                public void onCanceled(EasyImage.ImageSource source, int type) {
//                    //Batalkan penanganan, Anda mungkin ingin menghapus foto yang diambil jika dibatalkan
//                }
//            });
            } catch (Exception e) {

            }
        }
    }
}
