package com.azis.skripsiproject.Admin.LaporPerbaikan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.azis.skripsiproject.Controller.Perbaikan.DataItemPengajuan;
import com.azis.skripsiproject.R;

import java.util.List;

public class AdapterLaporan extends ArrayAdapter<DataItemPengajuan> {

    Context context;
    List<DataItemPengajuan> arrayListDataItemAdmin;

    public AdapterLaporan(@NonNull Context context, List<DataItemPengajuan> arrayListDataItemAdmin) {
        super(context, R.layout.custom_list_laporan, arrayListDataItemAdmin);

        this.context = context;
        this.arrayListDataItemAdmin = arrayListDataItemAdmin;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_laporan, null, true);

        TextView tvPengajuan = view.findViewById(R.id.cll_pengajuan);
        TextView tvNama = view.findViewById(R.id.cll_nama);
        TextView tvJenis = view.findViewById(R.id.cll_jenis);
        TextView tvTanggal = view.findViewById(R.id.cll_tanggal);
        TextView tvStatus = view.findViewById(R.id.cll_status);

        tvPengajuan.setText(arrayListDataItemAdmin.get(position).getId());
        tvNama.setText(arrayListDataItemAdmin.get(position).getNama_user());
        tvJenis.setText(arrayListDataItemAdmin.get(position).getJenis());
        tvStatus.setText(arrayListDataItemAdmin.get(position).getStatus());
        tvTanggal.setText(arrayListDataItemAdmin.get(position).getTanggal());

        return view;
    }

}
