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

public class AdapterPengajuanProses extends ArrayAdapter<DataItemPengajuan> {

    Context context;
    List<DataItemPengajuan> arrayListDataPengajuan;

    public AdapterPengajuanProses(@NonNull Context context, List<DataItemPengajuan> arrayListDataPengajuan) {
        super(context, R.layout.custom_list_proses, arrayListDataPengajuan);

        this.context = context;
        this.arrayListDataPengajuan = arrayListDataPengajuan;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_proses, null, true);

        TextView tvId = view.findViewById(R.id.clp_pengajuan);
        TextView tvJenis = view.findViewById(R.id.clp_jenis);
        TextView tvTipe = view.findViewById(R.id.clp_tipe);
        TextView tvStatus = view.findViewById(R.id.clp_status);
        TextView tvTanggal = view.findViewById(R.id.clp_tanggal);

        tvId.setText(arrayListDataPengajuan.get(position).getId());
        tvJenis.setText(arrayListDataPengajuan.get(position).getJenis());
        tvTipe.setText(arrayListDataPengajuan.get(position).getTipe());
        tvStatus.setText(arrayListDataPengajuan.get(position).getStatus());
        tvTanggal.setText(arrayListDataPengajuan.get(position).getTanggal());

        return view;
    }
}
