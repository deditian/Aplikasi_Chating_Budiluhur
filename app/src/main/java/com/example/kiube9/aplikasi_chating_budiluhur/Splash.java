package com.example.kiube9.aplikasi_chating_budiluhur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Kiube9 on 10/26/2016.
 */

public class Splash extends Activity {
    public ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Email = "email_student";
    public static final String Nama = "nama_email";

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();


        // String halo = firebaseAuth.getCurrentUser().getEmail().toString();
        SharedPreferences prefs = this.getSharedPreferences("preferencename", 0);
//        Toast.makeText(getBaseContext(), "email: " + prefs.getString("email", "-"), Toast.LENGTH_LONG)
//                .show();
        String halo = prefs.getString("email", "-");
        String passwordnya = prefs.getString("pass", "-");


        Log.i("test", "user:" + halo + "; password:" + passwordnya);
        String user[] = halo.trim().split("@");

        if (user.length > 1) {
            if (user[1].equals("budiluhur.ac.id")) {
                finish();
                startActivity(new Intent(getApplicationContext(), Roomnya_Dosen.class));
            } else if (user[1].equals("student.budiluhur.ac.id")) {
                finish();
                startActivity(new Intent(getApplicationContext(), Roomnya_Mahasiswa.class));
            }
        } else {
            StartAnimations();
        }

    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.img_spalsh);
        iv.clearAnimation();
        iv.startAnimation(anim);


        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splash.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splash.this.finish();

                } catch (InterruptedException e) {

                } finally {
                    Splash.this.finish();
                }
            }
        };
        splashTread.start();

    }

}
