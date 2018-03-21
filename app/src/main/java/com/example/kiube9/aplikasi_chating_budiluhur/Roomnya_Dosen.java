package com.example.kiube9.aplikasi_chating_budiluhur;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Roomnya_Dosen extends AppCompatActivity {
    private Button add_room;
    private EditText room_name;
    private ListView listView;
    private String name;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomnya_dosen);
        firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences prefs = this.getSharedPreferences("preferencename", 0);
        String halo = prefs.getString("email", "-");

        Boolean check_is_login = prefs.getBoolean("is_login", false);

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        add_room = (Button) findViewById(R.id.btn_add_room);
        room_name = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        listView.setAdapter(arrayAdapter);

        if (check_is_login) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selamat Datang " + halo);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }

        add_room.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String room_name1 = room_name.getText().toString();
                if(room_name1.isEmpty()){
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(Roomnya_Dosen.this);
                    builder2.setTitle("Nama tidak boleh kosong").setNegativeButton("OK",null).setCancelable(false);
                    AlertDialog alertDialog1 = builder2.create();
                    alertDialog1.show();
                }
                else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(room_name.getText().toString(), "");
                    root.updateChildren(map);
                    room_name.setText("");
                }
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), Chat_Room_Dosen.class);
                i.putExtra("room_name",((TextView)view).getText().toString());
                i.putExtra("user_name",user.getEmail());
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

