package com.example.kiube9.aplikasi_chating_budiluhur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Dosen extends AppCompatActivity implements View.OnClickListener {

    private Button BtnSignIn_Dosen;
    private EditText edtEmail_Dosen;
    private EditText edtPassword_Dosen;
    private TextView txtSignUp_Dosen;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Animation Fade_in, Fade_out;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dosen);


        viewFlipper = (ViewFlipper) this.findViewById(R.id.backgroundViewFlipper);
        Fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        viewFlipper.setAnimation(Fade_in);
        viewFlipper.setAnimation(Fade_out);

        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();
        firebaseAuth = FirebaseAuth.getInstance();




        progressDialog = new ProgressDialog(this);
        BtnSignIn_Dosen = (Button) findViewById(R.id.btnSignIn);
        edtEmail_Dosen = (EditText) findViewById(R.id.editTextMail);
        edtPassword_Dosen = (EditText) findViewById(R.id.editTextPassword);
        txtSignUp_Dosen = (TextView) findViewById(R.id.lblViewSignUp);

        BtnSignIn_Dosen.setOnClickListener(this);
        txtSignUp_Dosen.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == BtnSignIn_Dosen) {
            User_Login();
        }
        if (v == txtSignUp_Dosen) {
            finish();
            startActivity(new Intent(this, Biodata_Dosen.class));
        }
    }

    private void User_Login() {
        String email = edtEmail_Dosen.getText().toString().trim();
        String belakang_email = "@budiluhur.ac.id";
        final String Gabung = email + belakang_email;
        final String password = edtPassword_Dosen.getText().toString().trim();





        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty((password))) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("Login Dosen...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(Gabung, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        SharedPreferences app_preferences = getSharedPreferences("preferencename", 0);
                        SharedPreferences.Editor editor = app_preferences.edit();
                        editor.putString("email", Gabung);
                        editor.putString("pass", password);
                        editor.putBoolean("is_login", true);
                        editor.commit();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Roomnya_Dosen.class));
                    }
                    else {
                        Toast.makeText(Login_Dosen.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), Menu_pilih.class));
    }
}
