package com.example.kiube9.aplikasi_chating_budiluhur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.android.gms.internal.zzs.TAG;

public class Biodata_Mahasiswa extends Activity implements View.OnClickListener{
     EditText edtNamaMahasiswa, edtNim, edtNoTelp, edtPassword, edt_Validation_Mahasiswa;
      TextView signIn;
     Button btnRegister, btn_Ok_Mahasiswa;
    private ProgressDialog progressDialogq;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata__mahasiswa);
        edtNim = (EditText)findViewById(R.id.edtNim);
        edtNamaMahasiswa = (EditText) findViewById(R.id.edtNama);
        edtNoTelp = (EditText) findViewById(R.id.edtNoHp);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnRegister = (Button) findViewById(R.id.btnRegis);
        signIn = (TextView) findViewById(R.id.lblVwSigning);
        btnRegister.setOnClickListener(this);
        signIn.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialogq = new ProgressDialog(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v==btnRegister){
            String nim = edtNim.getText().toString().trim();
            String key_fakultas ="@student.budiluhur.ac.id";
            String nim_Key_Fakultas = nim+key_fakultas;
             String nama = edtNamaMahasiswa.getText().toString();
            String noTelp = edtNoTelp.getText().toString();
           final String Pass = edtPassword.getText().toString();
            String Gabung = nim+noTelp+Pass;

         //   final String Karakter_10 = new String(chiperText.substring(0,10));

            if(nama.isEmpty()||nim.isEmpty()||noTelp.isEmpty()||Pass.isEmpty()){
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Data tidak boleh kosong").setNegativeButton("OK",null).setCancelable(false);
                AlertDialog alertDialog1 = builder2.create();
                alertDialog1.show();
            }
            else
            {


                String chiperText = Kripto_Registrasi.encryptionMessage(Gabung);
                final String Karakter_10 = chiperText.substring(0,10);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(Biodata_Mahasiswa.this).inflate(R.layout.dialog_login_mahasiswa, null);
                AlertDialog.Builder builder1 = builder.setTitle("Validation Code : \n(Please Check your Email)")
                        .setView(view);
                final AlertDialog alertDialog = builder1.create();
                alertDialog.show();
                edt_Validation_Mahasiswa = (EditText)alertDialog.findViewById(R.id.edt_validation_mahasiswa);
                btn_Ok_Mahasiswa = (Button)alertDialog.findViewById(R.id.btn_ok_mahasiswa);
                btn_Ok_Mahasiswa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final  String validation_mahasiswa = edt_Validation_Mahasiswa.getText().toString();
                        if(validation_mahasiswa.equals(Karakter_10)){
                            Toast.makeText(getApplicationContext(),"Anda Berhasil Registrasi",Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(Biodata_Mahasiswa.this,Login_Mahasiswa.class);
                            startActivity(intent);

                            registerUser();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Code Tidak Sama",Toast.LENGTH_LONG).show();
                          //  Log.i("test","TIDAK SAMA :"+validation_mahasiswa+": :"+chiperText+":");
                        }
                    }
                });
                SendMail sm = new SendMail(this, nim_Key_Fakultas, "Chat Budi Luhur Configuration", "Welcome, "+nama +"\nSalam Budi Luhur Silahkan salin Kode tersebut ke app\n" +Karakter_10+"\n Terima Kasih");
                sm.execute();
              }
        }
        if (v==signIn){
            finish();
            Intent i = new Intent(Biodata_Mahasiswa.this,Login_Mahasiswa.class);
            startActivity(i);
        }
    }

    public void registerUser() {
        String nim1 = edtNim.getText().toString().trim()+"@student.budiluhur.ac.id";
         String Pass1 = edtPassword.getText().toString().trim();
        progressDialogq.setMessage("Registered Mahasiswa...");
        progressDialogq.show();

        firebaseAuth.createUserWithEmailAndPassword(nim1, Pass1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user udah sukses buat register
                            finish();
                            startActivity(new Intent(getApplicationContext(), Roomnya_Mahasiswa.class));
                        } else {
                            progressDialogq.dismiss();
                            Toast.makeText(Biodata_Mahasiswa.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), Login_Mahasiswa.class));
    }
}
