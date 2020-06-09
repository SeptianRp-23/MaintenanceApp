package com.azis.skripsiproject.Controller;

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

public class AdapterDataAdmin extends ArrayAdapter<DataItemAdmin> {

    Context context;
    List<DataItemAdmin> arrayListDataItemAdmin;

    public AdapterDataAdmin(@NonNull Context context, List<DataItemAdmin> arrayListDataItemAdmin) {
        super(context, R.layout.custom_list_data_admin, arrayListDataItemAdmin);

        this.context = context;
        this.arrayListDataItemAdmin = arrayListDataItemAdmin;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_data_admin, null, true);

        TextView tvEmail = view.findViewById(R.id.txt_email_adm);
        TextView tvNama = view.findViewById(R.id.txt_nama_adm);
        TextView tvStatus = view.findViewById(R.id.txt_status_adm);

        tvEmail.setText(arrayListDataItemAdmin.get(position).getNo_inventaris());
        tvNama.setText(arrayListDataItemAdmin.get(position).getJenis());
        tvStatus.setText(arrayListDataItemAdmin.get(position).getPengguna());

        return view;
    }
}
