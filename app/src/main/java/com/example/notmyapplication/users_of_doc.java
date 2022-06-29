package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

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
public class
users_of_doc extends AppCompatActivity implements interface_for_adapter {
    boolean updated= false;
    RecyclerView recyclerView;
    user u;
    ArrayList<user> UsersList;
    SharedPreferences spU, spD;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_of_doc);
        recyclerView = findViewById(R.id.UserList2);
        firebaseAuth = FirebaseAuth.getInstance();
        spU = getSharedPreferences("users", 0);
        spD = getSharedPreferences("Doctors", 0);
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { //  פעולה שאחראית על גילוי בחירת האופציה בתפריט שהמשתמש בחר ומפעילה אותו לפי הבחירה המניו יהיה לסוג דוק
                switch (item.getItemId()) {

                    case R.id.logout:
                        if (spU.getBoolean("isChecked", false)) {
                            SharedPreferences.Editor editor1 = spU.edit();
                            editor1.putBoolean("isChecked", false);
                            editor1.commit();
                        } else if (spD.getBoolean("isChecked", false)) {
                            SharedPreferences.Editor editor2 = spD.edit();
                            editor2.putBoolean("isChecked", false);
                            editor2.commit();
                        }
                        firebaseAuth.signOut();
                        finish();
                        Intent go = new Intent(users_of_doc.this, MainActivity.class);
                        startActivity(go);
                        break;

                    case R.id.userList:
                        Intent i3 = new Intent(users_of_doc.this, users_of_doc.class);
                        i3.putExtra("from users of doc", true);
                        startActivity(i3);
                        break;

                    case R.id.AddUser:

                        Intent i4 = new Intent(users_of_doc.this, ConnectDocToUser.class);
                        i4.putExtra("from users of doc", true);
                        startActivity(i4);

                        break;
                }
                return true;
            }
        });

        UsersList = new ArrayList<user>();// רשימת משתמשים של דוקטור
        DUL_adapter adapter = new DUL_adapter(users_of_doc.this, UsersList, this);


        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("Doctors", MODE_PRIVATE);
        String email = (sharedPreferences.getString("Email","default"));
         ArrayList<String> keylist = new ArrayList<>();

        FirebaseDatabase rebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Doctors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Doc d;
                    d = singleSnapshot.getValue(Doc.class);

                    if (d.getDocEmail().equals(email)) {

                        if (d.getUserList() != null) {
                            for (int i = 0; i < d.getUserList().size(); i++)
                                keylist.add((String) d.getUserList().get(i));

                        }
                    }

                    for (int i = 0; i < keylist.size(); i++){

                        String key=keylist.get(i);

                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("users");

                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    user u;
                                    u = singleSnapshot.getValue(user.class);

                                    if (u.getKey().equals(key)) {
                                        Toast.makeText(users_of_doc.this,
                                                u.getUserName(), Toast.LENGTH_LONG).show();
                                        UsersList.add(u);
                                    }
                                }
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(users_of_doc.this));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(users_of_doc.this,
                                        " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(users_of_doc.this,
                        " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        for (int i = 0; i < keylist.size(); i++){

            String key=keylist.get(i);
            rebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference1 = firebaseDatabase.getReference("Users");

            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        user u;
                        u = singleSnapshot.getValue(user.class);

                        if (u.getKey().equals(key)) {
                            UsersList.add(u);
                        }
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(users_of_doc.this));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(users_of_doc.this,
                            " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }



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
        Intent in =new Intent(this, UserPrescriptioDocView.class);

        in.putExtra("ukey", UsersList.get(position).getKey());
        startActivity(in);

    }
}



