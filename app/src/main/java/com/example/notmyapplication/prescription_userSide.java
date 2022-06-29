package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// רשימה הכוללת דרכה מחברים דוקטור ליוסר
public class prescription_userSide extends AppCompatActivity implements interface_for_adapter, View.OnClickListener {

    Button b;
    RecyclerView recyclerView;
    user u;
    ArrayList<Meds> medsList;
    BottomNavigationView bottomNav;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    SharedPreferences spU, spD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_user_side);
        recyclerView = findViewById(R.id.MedsUserSide);
        firebaseAuth = FirebaseAuth.getInstance();
        spU = getSharedPreferences("users", 0);
        spD = getSharedPreferences("Doctors", 0);
        bottomNav = (BottomNavigationView) findViewById(R.id.user_bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.logout:
                        if (spU.getBoolean("isChecked", true)) {
                            SharedPreferences.Editor editor1 = spU.edit();
                            editor1.putBoolean("isChecked", false);
                            editor1.commit();
                        } else if (spD.getBoolean("isChecked", true)) {
                            SharedPreferences.Editor editor2 = spD.edit();
                            editor2.putBoolean("isChecked", false);
                            editor2.commit();
                        }
                        firebaseAuth.signOut();
                        finish();
                        Intent go = new Intent(prescription_userSide.this, MainActivity.class);
                        startActivity(go);
                        break;
                    case R.id.chat:
                        Intent i1 = new Intent(prescription_userSide.this, chat.class);
                        i1.putExtra("from prescription_userSide", true);
                        startActivity(i1);
                        break;



                    case R.id.prescription:
                        break;


                }
                return true;
            }
        });

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("users", MODE_PRIVATE);
        String email = (sharedPreferences.getString("Email", "default"));



        medsList = new ArrayList<Meds>();
        Full_med_adapter adapter = new Full_med_adapter(prescription_userSide.this, medsList, this);

        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    if (singleSnapshot.getValue() != null) {
                        u = singleSnapshot.getValue(user.class);
                        if (u.getEmail().equals(email)) {
                            if(u.getMedlist()!=null) {
                                for(int i=0;i<u.getMedlist().size();i++)
                                medsList.add(u.getMedlist().get(i));
                            }

                        }
                    }


                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(prescription_userSide.this));

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }









    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(int position) {

    }
}