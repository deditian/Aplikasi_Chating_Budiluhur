package com.example.kiube9.aplikasi_chating_budiluhur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class Login_Mahasiswa extends AppCompatActivity implements View.OnClickListener {

    private Button BtnSignIn;
    private EditText edtEmail;
    private EditText edtPassword;
    private TextView txtSignUp;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Animation Fade_in, Fade_out;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mahasiswa);


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
        BtnSignIn = (Button) findViewById(R.id.btnSignIn);
        edtEmail = (EditText) findViewById(R.id.editTextMail);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        txtSignUp = (TextView) findViewById(R.id.lblViewSignUp);

        BtnSignIn.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == BtnSignIn) {
            User_Login();
        }

        if (v == txtSignUp) {
            finish();
            startActivity(new Intent(this, Biodata_Mahasiswa.class));
        }
    }

    private void User_Login() {
        String email = edtEmail.getText().toString().trim();
        String belakang_email = "@student.budiluhur.ac.id";
        final String gabung = email + belakang_email;
        final String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter NIM", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("Login Mahasiswa...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(gabung, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        SharedPreferences app_preferences = getSharedPreferences("preferencename", 0);
                        SharedPreferences.Editor editor = app_preferences.edit();
                        editor.putString("email", gabung);
                        editor.putString("pass", password);
                        editor.putBoolean("is_login", true);
                        editor.commit();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Roomnya_Mahasiswa.class));
                    }
                    else {
                        Toast.makeText(Login_Mahasiswa.this, "Login Failed", Toast.LENGTH_SHORT).show();
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
