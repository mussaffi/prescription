package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
// כרגע אין שימוש
public class Doc_User_List extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 1;
    ArrayList<user> rewardArrayList1,rewardArrayList2,rewardArrayList3;
    RecyclerView list_rewards1,list_rewards2,list_rewards3;
    DatabaseReference databaseReference;
    user r;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_user_list);
        try
        {
         //   view_a_list_rewards();
        }
        catch (Exception e)
        {
            Toast.makeText(Doc_User_List.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }
}