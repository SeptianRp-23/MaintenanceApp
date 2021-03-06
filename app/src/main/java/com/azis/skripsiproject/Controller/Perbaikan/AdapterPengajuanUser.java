package com.azis.skripsiproject.Controller.Perbaikan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.azis.skripsiproject.R;

import java.util.List;

public class AdapterPengajuanUser extends ArrayAdapter<DataItemPengajuan> {
    Context context;
    List<DataItemPengajuan> arrayListDataPengajuan;

    public AdapterPengajuanUser(@NonNull Context context, List<DataItemPengajuan> arrayListDataPengajuan) {
        super(context, R.layout.custom_show_pengajuan, arrayListDataPengajuan);

        this.context = context;
        this.arrayListDataPengajuan = arrayListDataPengajuan;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_show_pengajuan, null, true);

        TextView tvId = view.findViewById(R.id.txt_pengajuan);
        TextView tvJenis = view.findViewById(R.id.csp_jenis);
        TextView tvTipe = view.findViewById(R.id.csp_tipe);
        TextView tvStatus = view.findViewById(R.id.csp_status);
        TextView tvTgl = view.findViewById(R.id.csp_tanggal);

        tvId.setText(arrayListDataPengajuan.get(position).getId());
        tvJenis.setText(arrayListDataPengajuan.get(position).getJenis());
        tvTipe.setText(arrayListDataPengajuan.get(position).getTipe());
        tvStatus.setText(arrayListDataPengajuan.get(position).getStatus());
        tvTgl.setText(arrayListDataPengajuan.get(position).getTanggal());

        final String txtStatus = tvStatus.getText().toString().trim();
        if (txtStatus.equals("Di Tolak")){
//            tvStatus.setBackgroundColor(R.color.colorMerah);
            tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorMerah));
        }
        else if (txtStatus.equals("Di Setujui")){
            tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }

        return view;
    }
}
