package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notmyapplication.DUL_adapter;
import com.example.notmyapplication.Doc;
import com.example.notmyapplication.R;
import com.example.notmyapplication.doc_reg;
import com.example.notmyapplication.interface_for_adapter;
import com.example.notmyapplication.user;
import com.example.notmyapplication.user_login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class UserPrescriptioDocView extends AppCompatActivity implements interface_for_adapter, View.OnClickListener {
    String userKey;
    Button b;
    RecyclerView recyclerView;
    user u;
    ArrayList<Meds> medsList;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prescriptio_doc_view);
        recyclerView = findViewById(R.id.MedsDocSide);
        b=findViewById(R.id.addsMedToUser);


        Intent in=getIntent();//tpd=toPrescriptionDoc
        userKey= in.getExtras().getString("ukey");


        medsList = new ArrayList<Meds>();
        Full_med_adapter adapter = new Full_med_adapter(UserPrescriptioDocView.this, medsList, this);

        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    if (singleSnapshot.getValue() != null) {
                        u = singleSnapshot.getValue(user.class);
                        if (u.getKey().equals(userKey)) {
                            if(u.getMedlist()!=null) {
                                for(int i=0;i<u.getMedlist().size();i++)
                                    medsList.add(u.getMedlist().get(i));


                        }
                    }

                }

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(UserPrescriptioDocView.this));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guest_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//פה לחבר את האופציה לבוא לכאן עם התפריט
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.login) {
            Intent i = new Intent(this, user_login.class);
            startActivity(i);
        } else if (id == R.id.signup) {
            Intent i = new Intent(this, doc_reg.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            Intent i = new Intent(this, doc_reg.class);
            startActivity(i);
        }
        return true;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClick(View v) {
        Intent in =new Intent(this, med_select_for_doc.class);
          in.putExtra("ukey", userKey);
        startActivity(in);
    }
}

