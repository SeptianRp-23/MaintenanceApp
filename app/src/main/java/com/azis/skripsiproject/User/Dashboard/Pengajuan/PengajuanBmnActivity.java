package com.azis.skripsiproject.User.Dashboard.Pengajuan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.azis.skripsiproject.R;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PengajuanBmnActivity extends AppCompatActivity {

    String myFormat = "dd-MM-yyy hh:mm a";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private static final String TAG = "PengajuanBmnActivity";
    int position;
    EditText etNo, etjenis, etTipe, etPengguna, etTanggal, etUraian, etkomponen, etbiaya;
    ImageView imgbawah1,imgatas1,imgbawah2,imgatas2,imgbawah3,imgatas3,imgfoto;
    Button btnpilih,btnkirim;
    LinearLayout lin1, lin2, lin3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_bmn);

        etNo = findViewById(R.id.et_noBmn);
        etjenis = findViewById(R.id.et_jenis);
        etTipe = findViewById(R.id.et_tipe);
        etPengguna = findViewById(R.id.et_pengguna);

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

        //SetData
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        etNo.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getNo_inventaris());
        etjenis.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getJenis());
        etTipe.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getTipe());
        etPengguna.setText(PilihBarangActivity.dataItemAdminArrayList.get(position).getPengguna());
    }
}