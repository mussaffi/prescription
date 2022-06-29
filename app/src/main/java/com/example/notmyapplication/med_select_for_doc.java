package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

// רשימה הכוללת דרכה מחברים דוקטור ליוסר
public class med_select_for_doc extends AppCompatActivity implements View.OnClickListener, interface_for_adapter {


    String userKey;
    RecyclerView recyclerView;
    Meds m;
    ArrayList<Meds> medsList;
    AlertDialog.Builder builder;
    DatabaseReference databaseReference;
    double minteger = 0;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    boolean updated = false;
    TextView name;
    TextView price1;
    EditText amount1;
    TextView exDate;
    Button add;
    Button exit;
    AlertDialog dialog;
    private static final String TAG = "AddMed";
    DatePickerDialog.OnDateSetListener mDateSetListener_ex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_select_for_doc);
        recyclerView = findViewById(R.id.medSelectForDoc);
        Intent in = getIntent();
        userKey = in.getExtras().getString("ukey");

        medsList = new ArrayList<Meds>();
        med_adapter adapter = new med_adapter(med_select_for_doc.this, medsList, this);

        databaseReference = firebaseDatabase.getReference("Meds");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    if (singleSnapshot.getValue() != null) {
                        m = singleSnapshot.getValue(Meds.class);
                        medsList.add(m);
                    }
                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(med_select_for_doc.this));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createNewContactDialog(int pos) {//פותח את הפופ אפ של עדכון תרופה שבו הרופא יכניס את התאריך והכמות
        minteger = 0;
        builder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.list_popup_layout, null);

        name = (TextView) contactPopupView.findViewById(R.id.name);
        price1 = (TextView) contactPopupView.findViewById(R.id.price1);
        amount1 = (EditText) contactPopupView.findViewById(R.id.amount1);
        exDate = (TextView) contactPopupView.findViewById(R.id.exDate);

        name.setText(medsList.get(pos).getMedName());
        price1.setText(medsList.get(pos).getPrice() + " ");

        exDate = (TextView) contactPopupView.findViewById(R.id.exDate);
        exDate.setOnClickListener(this);
        mDateSetListener_ex = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                exDate.setText(date);
            }
        };

        add = (Button) contactPopupView.findViewById(R.id.add);
        exit = (Button) contactPopupView.findViewById(R.id.exit);

        builder.setView(contactPopupView);
        dialog = builder.create();
        dialog.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == add) {
                    //define save button here// לשנות אחכ
                    Meds m = new Meds(name.getText().toString(), Double.parseDouble(price1.getText().toString()), exDate.getText().toString(), Integer.parseInt(amount1.getText().toString()), medsList.get(pos).getPicName());
                    Toast.makeText(med_select_for_doc.this,
                            "ok", Toast.LENGTH_LONG).show();
                    addmed(m, pos);


                }
                dialog.dismiss();
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define cancel button here
                dialog.dismiss();
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
        updated = false;
        createNewContactDialog(position);

    }

    @Override
    public void onClick(View v) {
        if (v == exDate) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    med_select_for_doc.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener_ex,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        }

    }

    public void addmed(Meds m, int pos) {
        FirebaseDatabase rebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    user u;
                    u = singleSnapshot.getValue(user.class);


                    if (u.getKey().equals(userKey)) {

                        if (u.getMedlist() != null) {
                            boolean exist = false;
                            for (int i = 0; i < u.getMedlist().size(); i++)
                                if (u.getMedlist().get(i).getMedName() == (m.getMedName())) {
                                    exist = true;
                                }

                            if (exist == false)
                                u.addToMedlist(m);

                            //להוסף טוסט שהתרופה כבר קיים ברשימה של היוסר
                        } else {
                            ArrayList<Meds> temp = new ArrayList();
                            temp.add(m);
                            u.setMedlist(temp);
                        }
                        if (updated == false) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(u.getKey(), u);
                            updated = true;
                            //databaseReference.child(d.getDocKey()).child("userList").setValue(d.getUserList());
                            databaseReference.updateChildren(map);

                            /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                                    sendSMS(u.getPhone());
                                }else{
                                    requestPermissions(new String[] {Manifest.permission.SEND_SMS},1);
                                }
                            }*/

                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(med_select_for_doc.this,
                        " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    /*private void sendSMS(String phone) {
        String SMS = "נוספה תופה חדשה למרשמך ";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("(650) 555-1212", null, SMS, null, null);
            Toast.makeText(this, "message is sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Failed to senf message", Toast.LENGTH_SHORT).show();

        }
    }*/
}


