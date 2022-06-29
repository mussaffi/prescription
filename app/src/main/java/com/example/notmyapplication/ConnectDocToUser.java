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
    import java.util.HashMap;

    // רשימה הכוללת דרכה מחברים דוקטור ליוסר
    public class ConnectDocToUser extends AppCompatActivity implements interface_for_adapter {
        boolean updated= false;
        RecyclerView recyclerView;
        user u;
        ArrayList<user> UsersList;
        SharedPreferences spU, spD;
        DatabaseReference databaseReference;
        BottomNavigationView bottomNav;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_connect_doc_to_user);
            spU = getSharedPreferences("users", 0);
            spD = getSharedPreferences("Doctors", 0);
            recyclerView = findViewById(R.id.UserList);
            bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            Menu menu = bottomNav.getMenu();
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);
            firebaseAuth = FirebaseAuth.getInstance();

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
                            Intent go = new Intent(ConnectDocToUser.this, MainActivity.class);
                            startActivity(go);
                            break;

                        case R.id.userList:
                            Intent i3 = new Intent(ConnectDocToUser.this, users_of_doc.class);
                            i3.putExtra("from userList", true);
                            startActivity(i3);
                            break;

                        case R.id.AddUser:

                            Intent i4 = new Intent(ConnectDocToUser.this, ConnectDocToUser.class);
                            i4.putExtra("from userList", true);
                            startActivity(i4);

                            break;
                    }
                    return true;
                }
            });

            UsersList = new ArrayList<user>();
            DUL_adapter adapter = new DUL_adapter(ConnectDocToUser.this, UsersList, this);

            databaseReference = firebaseDatabase.getReference("users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot singleSnapshot : snapshot.getChildren()) {

                        if (singleSnapshot.getValue() != null) {
                            u = singleSnapshot.getValue(user.class);
                            UsersList.add(u);

                        }
                    }

                  recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ConnectDocToUser.this));


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.bottom_menu, menu);
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
            updated=false;
            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences("Doctors", MODE_PRIVATE);
            String email = (sharedPreferences.getString("Email","default"));


            FirebaseDatabase rebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Doctors");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Doc d;
                           d= singleSnapshot.getValue(Doc.class);


                        if (d.getDocEmail().equals(email)) {

                            if (d.getUserList() != null) {
                                boolean exist = false;
                                for (int i = 0; i < d.getUserList().size(); i++)
                                    if (d.getUserList().get(i).equals(UsersList.get(position).getKey())) {
                                        exist = true;
                                    }

                            if (exist == false)
                                d.AddUserKeyToDoc(UsersList.get(position).getKey());
                                Toast.makeText(ConnectDocToUser.this, "patient already connected", Toast.LENGTH_LONG).show();

                            }
                            else{
                                ArrayList<String> temp = new ArrayList();
                                temp.add(UsersList.get(position).getKey());
                                d.setUserList(temp);
                            }
                            if(updated==false) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put(d.getDocKey(), d);
                                //databaseReference.child(d.getDocKey()).child("userList").setValue(d.getUserList());
                                databaseReference.updateChildren(map);
                                updated=true;
                            }
                        }
    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ConnectDocToUser.this,
                            " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }



