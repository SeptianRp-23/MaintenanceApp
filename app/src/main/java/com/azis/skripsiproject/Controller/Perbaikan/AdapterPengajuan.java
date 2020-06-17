package com.azis.skripsiproject.Controller.Perbaikan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.azis.skripsiproject.R;
import java.util.List;

public class AdapterPengajuan extends ArrayAdapter<DataItemPengajuan> {

    Context context;
    List<DataItemPengajuan> arrayListDataPengajuan;

    public AdapterPengajuan(@NonNull Context context, List<DataItemPengajuan> arrayListDataPengajuan) {
        super(context, R.layout.custom_list_perbaikan, arrayListDataPengajuan);

        this.context = context;
        this.arrayListDataPengajuan = arrayListDataPengajuan;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_perbaikan, null, true);

        TextView tvId = view.findViewById(R.id.id_barang);
        TextView tvNama = view.findViewById(R.id.txt_nama_user);
        TextView tvTanggal = view.findViewById(R.id.txt_tangal);
        TextView tvJenis = view.findViewById(R.id.txt_jenis);
        TextView tvTipe = view.findViewById(R.id.txt_tipe);

        tvId.setText(arrayListDataPengajuan.get(position).getId_barang());
        tvNama.setText(arrayListDataPengajuan.get(position).getNama_user());
        tvTanggal.setText(arrayListDataPengajuan.get(position).getTanggal());
        tvJenis.setText(arrayListDataPengajuan.get(position).getJenis());
        tvTipe.setText(arrayListDataPengajuan.get(position).getTipe());

        return view;
    }
}
