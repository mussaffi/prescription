package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    SharedPreferences spU, spD;
    FirebaseAuth firebaseAuth;
    Intent in,i2, go;

    BottomNavigationView bottomNav;
    BottomNavigationView bottomNavUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //פעולה היוצרת את האקטיביטי
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        in = getIntent();
        i2 = getIntent();
        go = getIntent();

        spU = getSharedPreferences("users", 0);
        spD = getSharedPreferences("Doctors", 0);
        firebaseAuth = FirebaseAuth.getInstance();

        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.INVISIBLE);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavUser = (BottomNavigationView) findViewById(R.id.user_bottom_navigation);
        bottomNavUser.setVisibility(View.INVISIBLE);
        Menu menu2 = bottomNavUser.getMenu();
        MenuItem menuItem2 = menu2.getItem(0);
        menuItem.setChecked(true);


            if (spU.getBoolean("isChecked", false)||(in != null && in.getBooleanExtra("from user_login", false)) ||
                    (i2 != null && i2.getBooleanExtra("from prescription_userSide", false)) ||
                    (go != null && go.getBooleanExtra("from Chat", false))
            )

                bottomNavUser.setVisibility(View.VISIBLE);


            else if (spD.getBoolean("isChecked", true) ||(in != null && in.getBooleanExtra("from doc_login", false))||
                    go != null && go.getBooleanExtra("userList", false) ||
                    go != null && go.getBooleanExtra("from users of doc", false)) {
                bottomNav.setVisibility(View.VISIBLE);
            }
            else {
                bottomNavUser.setVisibility(View.INVISIBLE);
                bottomNav.setVisibility(View.INVISIBLE);

            }

        bottomNavUser.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { //  פעולה שאחראית על גילוי בחירת האופציה בתפריט שהמשתמש בחר ומפעילה אותו לפי הבחירה המניו יהיה לסוג יוסר
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
                        Intent go = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(go);
                        break;

                    case R.id.prescription:
                        Intent i3 = new Intent(MainActivity.this, prescription_userSide.class);
                        i3.putExtra("from MainActivity", true);
                        startActivity(i3);
                        break;

                    case R.id.chat:
                        Intent i4 = new Intent(MainActivity.this, chat.class);
                        i4.putExtra("from MainActivity", true);
                        startActivity(i4);
                        break;
                }
                return true;
            }
        });


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
                        Intent go = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(go);
                        break;

                    case R.id.userList:
                        Intent i3 = new Intent(MainActivity.this, users_of_doc.class);
                        i3.putExtra("from MainActivity", true);
                        startActivity(i3);
                        break;

                    case R.id.AddUser:

                        Intent i4 = new Intent(MainActivity.this, ConnectDocToUser.class);
                        i4.putExtra("from MainActivity", true);
                        startActivity(i4);

                        break;
                }
                return true;
            }
        });


        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                        Intent go = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(go);
                        break;

                    case R.id.userList:
                        Intent i3 = new Intent(MainActivity.this, users_of_doc.class);
                        i3.putExtra("from MainActivity", true);
                        startActivity(i3);
                        break;

                    case R.id.AddUser:

                        Intent i4 = new Intent(MainActivity.this, ConnectDocToUser.class);
                        i4.putExtra("from MainActivity", true);
                        startActivity(i4);

                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)// יוצר את המניו לפי איזה סוג משתמש מחובר
    {
        if (!spU.getBoolean("isChecked", false)
                || (!spD.getBoolean("isChecked", false)
                || !(in != null && in.getBooleanExtra("from user_login", false)
                || !(in != null && in.getBooleanExtra("from doc_login", false))))) {
            if (!(in != null && in.getBooleanExtra("from user_login", false)))
                getMenuInflater().inflate(R.menu.guest_menu, menu);

            return true;
        }
            else
                return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.login) {
            Intent i = new Intent(MainActivity.this, user_login.class);
            startActivity(i);
        } else if (id == R.id.signup) {
            Intent i = new Intent(MainActivity.this, UserRegister.class);
            startActivity(i);
        } else if (id == R.id.logout) {
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
            Intent go = new Intent(MainActivity.this, MainActivity.class);
            startActivity(go);
        } else if (id == R.id.docLogin) {
            Intent i = new Intent(MainActivity.this, doc_login.class);
            startActivity(i);
        } else if (id == R.id.docReg) {
            Intent i = new Intent(MainActivity.this, doc_reg.class);
            startActivity(i);
        } else if (id == R.id.prescription) {
            Intent i = new Intent(MainActivity.this, prescription_userSide.class);
            startActivity(i);
        } else if (id == R.id.chat) {
            Intent i = new Intent(MainActivity.this, chat.class);
            startActivity(i);
        }
        return true;
    }
}




