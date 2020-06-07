package com.azis.skripsiproject.User.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.azis.skripsiproject.Controller.SessionManager;
import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.R;
import com.azis.skripsiproject.User.Dashboard.User.ProfileActivity;
import com.azis.skripsiproject.User.Laporan.LaporanActivity;
import com.azis.skripsiproject.User.Status.StatusActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {


    private long backPressedTime;
    private Toast backToast;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String getID;

    EditText etjenis,etnama,ettahun,eturaian,etpengguna,etkomponen,etbiaya;
    ImageView imgbawah1,imgatas1,imgbawah2,imgatas2,imgbawah3,imgatas3,imgfoto;
    Button btnpilih,btnkirim;
    LinearLayout lin1, lin2, lin3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Dashboard");
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);

        //attribut 1
        imgatas1 = findViewById(R.id.show1_2);
        imgbawah1 = findViewById(R.id.show1);
        lin1 = findViewById(R.id.linear1);

        imgbawah1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin1.setVisibility(View.VISIBLE);
                imgatas1.setVisibility(View.VISIBLE);
                imgbawah1.setVisibility(View.GONE);
            }
        });

        imgatas1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin1.setVisibility(View.GONE);
                imgatas1.setVisibility(View.GONE);
                imgbawah1.setVisibility(View.VISIBLE);
            }
        });


//        attribut 2
        lin2 = findViewById(R.id.linear2);
        imgatas2 = findViewById(R.id.show2_2);
        imgbawah2 = findViewById(R.id.show2);

        imgbawah2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin2.setVisibility(View.VISIBLE);
                imgatas2.setVisibility(View.VISIBLE);
                imgbawah2.setVisibility(View.GONE);
            }
        });

        imgatas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin2.setVisibility(View.GONE);
                imgatas2.setVisibility(View.GONE);
                imgbawah2.setVisibility(View.VISIBLE);
            }
        });

//        attribut 3
        lin3 = findViewById(R.id.linear3);
        imgbawah3 = findViewById(R.id.show3);
        imgatas3 = findViewById(R.id.show3_2);

        imgbawah3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin3.setVisibility(View.VISIBLE);
                imgatas3.setVisibility(View.VISIBLE);
                imgbawah3.setVisibility(View.GONE);
            }
        });

        imgatas3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin3.setVisibility(View.GONE);
                imgatas3.setVisibility(View.GONE);
                imgbawah3.setVisibility(View.VISIBLE);
            }
        });
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.account:
                Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(i);
                return true;
            case R.id.logout:
                Toast.makeText(this, "Terimakasih", Toast.LENGTH_SHORT).show();
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public void user(View view) {
//        Intent intent = new Intent(MainActivity.this, TambahUserActivity.class);
//        startActivity(intent);
//    }
//    public void mobil(View view) {
//        Intent intent = new Intent(MainActivity.this, DataMobilActivity.class);
//        startActivity(intent);
//    }

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
        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        finish();
    }
}