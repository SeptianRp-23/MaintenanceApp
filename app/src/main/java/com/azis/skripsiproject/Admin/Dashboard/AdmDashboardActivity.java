package com.azis.skripsiproject.Admin.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.azis.skripsiproject.Admin.Laporan.AdmLaporanActivity;
import com.azis.skripsiproject.Admin.Peminjaman.AdmPeminjamanActivity;
import com.azis.skripsiproject.Admin.Perbaikan.AdmDataPerbaikanActivity;
import com.azis.skripsiproject.Admin.Profile.AdmAkunActivity;
import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.R;

import java.util.HashMap;

public class AdmDashboardActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String getID;
    Button btLogout;
    CardView card1, card2, card3, card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_dashboard);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);
        card1 = findViewById(R.id.card_perbaikan);
        card2 = findViewById(R.id.card_peminjaman);
        card3 = findViewById(R.id.card_admin);
        card4 = findViewById(R.id.card_laporan);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdmDashboardActivity.this, AdmDataPerbaikanActivity.class));
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdmDashboardActivity.this, AdmPeminjamanActivity.class));
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdmDashboardActivity.this, AdmAkunActivity.class));
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdmDashboardActivity.this, AdmLaporanActivity.class));
            }
        });

        btLogout = findViewById(R.id.logout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        else {
            backToast = Toast.makeText(this, "Tekan Lagi Untuk Keluar", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
//        new AlertDialog.Builder(this)
//                .setTitle("Really Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
////                        MainActivity.super.onBackPressed();
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                        System.exit(0);
//                    }
//                }).create().show();
    }


    private void logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
        editor.apply();
        startActivity(new Intent(AdmDashboardActivity.this, LoginActivity.class));
        finish();
    }
}