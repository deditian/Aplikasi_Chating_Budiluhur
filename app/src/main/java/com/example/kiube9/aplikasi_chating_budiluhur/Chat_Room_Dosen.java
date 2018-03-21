package com.example.kiube9.aplikasi_chating_budiluhur;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat_Room_Dosen extends AppCompatActivity {
    private Button btn_send_msg, logout;
    private EditText input_msg2;
    private TextView chat_conversation;
    private String user_name, room_name;

    private DatabaseReference root;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_dosen);
        btn_send_msg = (Button) findViewById(R.id.btn_send_dosen);

        input_msg2 = (EditText) findViewById(R.id.msg_input_dosen);
        chat_conversation = (TextView) findViewById(R.id.Textview_dosen);

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" Room - " + room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masuk_pesan2 = input_msg2.getText().toString();
                if(masuk_pesan2.isEmpty()){
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(Chat_Room_Dosen.this);
                    builder2.setTitle("Isi Pesan Anda").setNegativeButton("OK",null).setCancelable(false);
                    AlertDialog alertDialog1 = builder2.create();
                    alertDialog1.show();
                }else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = root.push().getKey();
                    root.updateChildren(map);
                    DatabaseReference message_root = root.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("name ", user_name);
                    String input = input_msg2.getText().toString();
                    String chiphertext = Kripto.encryptionMessage(input);
                    map2.put("msg ", chiphertext);
                    message_root.updateChildren(map2);
                    input_msg2.setText("");
                }
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String chat_msg,chat_user_msg;
    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator it = dataSnapshot.getChildren().iterator();
        while (it.hasNext()){
            chat_msg = (String) ((DataSnapshot)it.next()).getValue();
            chat_user_msg = (String) ((DataSnapshot) it.next()).getValue();
            String user[] = chat_user_msg.trim().split("@");
            String decryption = Kripto.decryptionMessage(chat_msg);
            chat_conversation.append(user[0] + " : "+ decryption+"\n");

        }
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), Roomnya_Dosen.class));
        SharedPreferences prefs = this.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_login", false);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_Logout){
            FirebaseAuth.getInstance().signOut();
            SharedPreferences app_preferences = getSharedPreferences("preferencename", 0);
            SharedPreferences.Editor editor = app_preferences.edit();

            editor.putString("email", "");
            editor.putString("pass", "");
            editor.commit();
            startActivity(new Intent(Chat_Room_Dosen.this, Menu_pilih.class));
            Toast.makeText(Chat_Room_Dosen.this, user_name + "\nLogout", Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}