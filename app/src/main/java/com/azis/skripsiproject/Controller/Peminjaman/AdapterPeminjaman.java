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

public class AdapterPeminjaman extends ArrayAdapter<DataItemPeminjaman> {

    Context context;
    List<DataItemPeminjaman> arrayListDataPeminjaman;

    public AdapterPeminjaman(@NonNull Context context, List<DataItemPeminjaman> arrayListDataPeminjaman) {
        super(context, R.layout.custom_peminjaman, arrayListDataPeminjaman);

        this.context = context;
        this.arrayListDataPeminjaman = arrayListDataPeminjaman;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_peminjaman, null, true);

        TextView tvNoInves = view.findViewById(R.id.txt_no_inves);
        TextView tvJenis = view.findViewById(R.id.txt_jenis);
        TextView tvTipe = view.findViewById(R.id.txt_tipe);
        TextView tvTanggal = view.findViewById(R.id.txt_tgl);
        TextView tvPengguna = view.findViewById(R.id.txt_nama);
        TextView tvPokja = view.findViewById(R.id.txt_pokja);

        tvNoInves.setText(arrayListDataPeminjaman.get(position).getNo_inventarisPmjn());
        tvJenis.setText(arrayListDataPeminjaman.get(position).getJenisPmjn());
        tvTipe.setText(arrayListDataPeminjaman.get(position).getTipePmjn());
        tvTanggal.setText(arrayListDataPeminjaman.get(position).getTanggalPmjn());
        tvPengguna.setText(arrayListDataPeminjaman.get(position).getPenggunaPmjn());
        tvPokja.setText(arrayListDataPeminjaman.get(position).getPokjaPmjn());

        return view;
    }
}
