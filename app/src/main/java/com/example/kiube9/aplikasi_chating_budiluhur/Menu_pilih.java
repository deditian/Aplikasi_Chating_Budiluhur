package com.example.kiube9.aplikasi_chating_budiluhur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu_pilih extends AppCompatActivity implements View.OnClickListener {
    public Button dosen, mahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pilih);
        dosen = (Button) findViewById(R.id.btn_dosen);
        mahasiswa = (Button) findViewById(R.id.btn_mahasiswa);
        dosen.setOnClickListener(this);
        mahasiswa.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == dosen) {
            finish();
            Intent i = new Intent(this, Login_Dosen.class);
            startActivity(i);
        }
        if (v == mahasiswa) {
            finish();
            Intent i = new Intent(this, Login_Mahasiswa.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {


        finish();
    }
}
