package com.azis.skripsiproject.Controller.Peminjaman;

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

public class AdapterBarang extends ArrayAdapter<DataItemBarang> {

    Context context;
    List<DataItemBarang> arrayListDataBarang;

    public AdapterBarang(@NonNull Context context, List<DataItemBarang> arrayListDataBarang) {
        super(context, R.layout.custom_barang, arrayListDataBarang);

        this.context = context;
        this.arrayListDataBarang = arrayListDataBarang;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_barang, null, true);

        TextView tvNoInves = view.findViewById(R.id.txt_inves_pilih);
        TextView tvJenis = view.findViewById(R.id.txt_pilih_jenis);
        TextView tvTipe = view.findViewById(R.id.txt_tipe_pilih);
        TextView tvTanggal = view.findViewById(R.id.txt_tgl_pilih);

        tvNoInves.setText(arrayListDataBarang.get(position).getNo_invesBrg());
        tvJenis.setText(arrayListDataBarang.get(position).getJenisBrg());
        tvTipe.setText(arrayListDataBarang.get(position).getTipeBrg());
        tvTanggal.setText(arrayListDataBarang.get(position).getTanggalBrg());

        return view;
    }
}
