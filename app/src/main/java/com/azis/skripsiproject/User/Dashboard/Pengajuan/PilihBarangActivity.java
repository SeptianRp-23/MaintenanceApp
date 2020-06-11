package com.azis.skripsiproject.User.Dashboard.Pengajuan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azis.skripsiproject.Controller.AdapterDataAdmin;
import com.azis.skripsiproject.Controller.DataItemAdmin;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.Server.Api;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PilihBarangActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String getId;
    ListView listViewAdm;
    AdapterDataAdmin adapterDataAdmin;
    public static ArrayList<DataItemAdmin> dataItemAdminArrayList = new ArrayList<>();
    private String ShowBarang = Api.URL_API + "showBarang.php";
    DataItemAdmin dataItemAdmin;
    MaterialEditText cari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_barang);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        listViewAdm = findViewById(R.id.myListviewAdm);
        adapterDataAdmin = new AdapterDataAdmin(this, dataItemAdminArrayList);
        listViewAdm.setAdapter(adapterDataAdmin);

        listViewAdm.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                startActivity(new Intent(getApplicationContext(), PengajuanBmnActivity.class)
                        .putExtra("position", position));

//                CharSequence[] dialogItem = {"View Data", "Edit Data", "Delete Data"};
//                builder.setTitle(dataItemAdminArrayList.get(position).getPengguna());
//                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        switch (i) {
//                            case 0:
//
//                                break;
//                            case 1:
//                                break;
//                            case 2:
//                                break;
//                        }
//                    }
//                });
//                builder.create().show();
            }
        });
        receiveData();
    }

    public void receiveData(){
        StringRequest request = new StringRequest(Request.Method.POST, ShowBarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataItemAdminArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String no_inventaris = object.getString("no_inventaris");
                                    String jenis = object.getString("jenis");
                                    String tipe = object.getString("tipe");
                                    String tanggal = object.getString("tanggal");
                                    String pengguna = object.getString("pengguna");

                                    dataItemAdmin = new DataItemAdmin(id, no_inventaris, jenis, tipe, tanggal, pengguna);
                                    dataItemAdminArrayList.add(dataItemAdmin);
                                    adapterDataAdmin.notifyDataSetChanged();
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PilihBarangActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_item, menu);
//
//        final MenuItem searchItem = menu.findItem(R.id.item_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                ArrayList<String> dataList = new ArrayList<>();
//                for (String data : dataItemAdminArrayList){
//                    if (data.toLowerCase().contains(newText.toLowerCase())){
//                        listViewAdm.add(data);
//                    }
//                }
//                return true;
//
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

    public void back(View view) {
        startActivity(new Intent(PilihBarangActivity.this, DashboardActivity.class));
    }
}