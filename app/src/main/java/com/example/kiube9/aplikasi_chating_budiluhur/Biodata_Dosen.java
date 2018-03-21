package com.example.kiube9.aplikasi_chating_budiluhur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
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

public class Biodata_Dosen extends Activity implements View.OnClickListener {
    EditText edtNama_Dosen, edtEmail_Dosen, edtNoTelp_Dosen, edtPassword_Dosen, edt_Validation_Dosen, edt_coba;
    TextView signIn;
    Button btnRegister_Dosen, btn_Ok_Dosen;
    public ProgressDialog progressDialog;
    public FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialogq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata__dosen);
        edtEmail_Dosen = (EditText) findViewById(R.id.edtEmail_Dosen);
        edtNama_Dosen = (EditText) findViewById(R.id.edtNama_Dosen);
        edtNoTelp_Dosen = (EditText) findViewById(R.id.edtNoHp_Dosen);
        edtPassword_Dosen = (EditText) findViewById(R.id.edtPassword_Dosen);
        btnRegister_Dosen = (Button) findViewById(R.id.btnRegis_Dosen);
        signIn = (TextView) findViewById(R.id.lblVwSigning_Dosen);
        btnRegister_Dosen.setOnClickListener(this);
        signIn.setOnClickListener(this);
        progressDialogq = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == btnRegister_Dosen) {
            String Email_Dosen = edtEmail_Dosen.getText().toString();
            String key_fakultas = "@budiluhur.ac.id";
            String namaemail_dosen_Key_Fakultas_ = Email_Dosen + key_fakultas;
            final String nama = edtNama_Dosen.getText().toString();
            String noTelp = edtNoTelp_Dosen.getText().toString();
            String Pass = edtPassword_Dosen.getText().toString();
            String Gabung = Email_Dosen + noTelp + Pass;



            if (nama.isEmpty() || Email_Dosen.isEmpty() || noTelp.isEmpty() || Pass.isEmpty()) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Data tidak boleh kosong").setNegativeButton("OK", null).setCancelable(false);
                AlertDialog alertDialog1 = builder2.create();
                alertDialog1.show();
            } else {

                String chiperText = Kripto_Registrasi.encryptionMessage(Gabung);
                final String Karakter_10 = chiperText.substring(0,10);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(Biodata_Dosen.this).inflate(R.layout.dialog_login_mahasiswa, null);
                AlertDialog.Builder builder1 = builder.setTitle("Validation Code")
                        .setView(view);
                final AlertDialog alertDialog = builder1.create();
                alertDialog.show();
                edt_Validation_Dosen = (EditText) alertDialog.findViewById(R.id.edt_validation_mahasiswa);
                btn_Ok_Dosen = (Button) alertDialog.findViewById(R.id.btn_ok_mahasiswa);
                btn_Ok_Dosen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String validation_dosen = edt_Validation_Dosen.getText().toString();
                        if (validation_dosen.equals(Karakter_10)) {
                            Toast.makeText(getApplicationContext(),"Anda Berhasil Registrasi",Toast.LENGTH_LONG).show();
                           finish();
                            Intent intent = new Intent(Biodata_Dosen.this, Login_Dosen.class);
                            startActivity(intent);
                            registerdosen();

                        } else {
                            Toast.makeText(getApplicationContext(), " Code Tidak Sama ", Toast.LENGTH_LONG).show();
                           // Log.i("test", "TIDAK SAMA :" + validation_dosen + ": :" + chiperText + ":");
                        }
                    }
                });
                SendMail sm = new SendMail(this, namaemail_dosen_Key_Fakultas_, "Chat Budi Luhur Configuraton", "Welcome, " + nama + "\nSalam Budi Luhur Silahkan salin Kode tersebut ke app\n" + Karakter_10 + "\n Terima Kasih");
                sm.execute();
            }
        }
        if (v == signIn) {
            finish();
            Intent i = new Intent(Biodata_Dosen.this, Login_Dosen.class);
            startActivity(i);
        }
    }

    public void registerdosen() {
        String Email_Dosen = edtEmail_Dosen.getText().toString().trim() + "@budiluhur.ac.id";
        String Pass = edtPassword_Dosen.getText().toString().trim();
        progressDialogq.setMessage("Registered Dosen ...");
        progressDialogq.show();

        firebaseAuth.createUserWithEmailAndPassword(Email_Dosen, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user udah sukses buat register
                            finish();
                            startActivity(new Intent(getApplicationContext(), Roomnya_Mahasiswa.class));
                        } else {
                            progressDialogq.dismiss();
                            Toast.makeText(Biodata_Dosen.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), Login_Dosen.class));
    }
}
