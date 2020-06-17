package com.azis.skripsiproject.Admin.Laporan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.azis.skripsiproject.R;
import com.azis.skripsiproject.User.Laporan.LaporanActivity;
import com.azis.skripsiproject.User.Status.StatusActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdmLaporanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_laporan);

        //ButtomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
//                        startActivity(new Intent(getApplicationContext(),
//                                TransaksiActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                    case R.id.status:
                        startActivity(new Intent(getApplicationContext(),
                                StatusActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.laporan:
                        startActivity(new Intent(getApplicationContext(),
                                LaporanActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //End ButtomNav
    }
}