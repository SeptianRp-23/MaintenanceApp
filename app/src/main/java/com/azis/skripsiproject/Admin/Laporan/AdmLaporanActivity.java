package com.azis.skripsiproject.Admin.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.azis.skripsiproject.Admin.Dashboard.AdmDashboardActivity;
import com.azis.skripsiproject.Admin.Peminjaman.AdmPeminjamanActivity;
import com.azis.skripsiproject.R;

import java.util.ArrayList;
import java.util.List;

public class AdmLaporanActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static List<PDFModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_laporan);

        recyclerView = findViewById(R.id.RV);
        list = new ArrayList<>();
        list.add(new PDFModel("Penggunaan_Barang","https://mydbskripsi.000webhostapp.com/files/Pengguna_Barang.pdf"));
        list.add(new PDFModel("Data_Barang", "https://mydbskripsi.000webhostapp.com/files/Data_Barang.pdf"));
        list.add(new PDFModel("Pengajuan_Bmn", "https://mydbskripsi.000webhostapp.com/files/Pengajuan_Bmn.pdf"));
        list.add(new PDFModel("Pengajuan_Famum", "https://mydbskripsi.000webhostapp.com/files/Pengajuan_Famum.pdf"));
        list.add(new PDFModel("Pengguna_Aplikasi", "https://mydbskripsi.000webhostapp.com/files/Semua_Pengajuan.pdf"));
        list.add(new PDFModel("Pekerjaan_Selesai", "https://mydbskripsi.000webhostapp.com/files/Tugas_Selesai.pdf"));

        recyclerView.setLayoutManager(new GridLayoutManager(AdmLaporanActivity.this, 2));

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(AdmLaporanActivity.this,PDFActivity.class);
                //intent.putExtra("url",list.get(position).getPdfUrl());
                intent.putExtra("position",position);
                startActivity(intent);
            }
        };
        PDFAdapter adapter = new PDFAdapter(list,this,itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdmLaporanActivity.this, AdmDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}