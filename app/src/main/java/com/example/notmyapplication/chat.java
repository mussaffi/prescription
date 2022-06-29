package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

public class chat extends AppCompatActivity
{
    ImageView sendMessege;
    EditText text;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    ArrayList<Message> listpost;
    ArrayAdapter<Message> lpadapter;
    ListView myListView;
    DatabaseReference myref;
    SharedPreferences spU, spD;
    ProgressDialog p;
    BottomNavigationView bottomNavUser;

    Message message, m;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        spU = getSharedPreferences("users", 0);
        spD = getSharedPreferences("Doctors", 0);

        bottomNavUser = (BottomNavigationView) findViewById(R.id.user_bottom_navigation);
        Menu menu = bottomNavUser.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavUser.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
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
                        Intent go = new Intent(chat.this, MainActivity.class);
                        startActivity(go);
                        break;

                    case R.id.prescription:
                        Intent i1 = new Intent(chat.this, prescription_userSide.class);
                        i1.putExtra("from Chat", true);
                        startActivity(i1);
                        break;


                    case R.id.chat:
                        break;


                }
                return true;
            }
        });


        message = new Message();
        myref = firebaseDatabase.getReference("Messages");
        list = new ArrayList<>();
        listpost = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        sendMessege = (ImageView) findViewById(R.id.send_image);
        sendMessege.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createMSG();
            }
        });
        text = (EditText) findViewById(R.id.user_message);
        myListView = (ListView) findViewById(R.id.list);
        lpadapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, listpost);

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    message = ds.getValue(Message.class);
                    list.add( message.getFromUser() + ": " + message.getText());
                    listpost.add(message);
                }
                myListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createMSG()
    {
        if (isValidate())
        {
            p = new ProgressDialog(this);
            p.setMessage("שולח..");
            p.show();
            //לשם חיבור לבסיס הנתונים
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            //הצבעה לתת ענף של משתמשים ("טבלה")
            DatabaseReference myref = firebaseDatabase.getReference("Messages");

            m = new Message(text.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")));
            Toast.makeText(chat.this, "sent", Toast.LENGTH_SHORT).show();
            myref.push().setValue(m);

            this.recreate();
            text.setText("");
        }
    }

    public boolean isValidate()
    {
        if (text.getText().toString().length() == 0)
        {
            text.setError("cant send empty messages");
            text.setFocusable(true);
            return false;
        }

        if (firebaseAuth.getInstance().getCurrentUser()==null)
            return false;
        return true;
    }

}