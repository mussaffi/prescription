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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class user_login extends AppCompatActivity implements View.OnClickListener {
    EditText name, email, pass;
    Button b3;
    String password;
    String mail;
    String Name;


    SharedPreferences sp;
    CheckBox rem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name=findViewById(R.id.loginName);
        email=findViewById(R.id.editTextTextEmailAddress);
        pass=findViewById(R.id.editTextTextPassword);
        b3=findViewById(R.id.button3);
        b3.setOnClickListener(this);
        sp=getSharedPreferences("users", 0);
        rem=findViewById(R.id.RememberMe);



    }

    public void logIn(){
        final ProgressDialog pd=ProgressDialog.show(this, "connecting", "please wait", true);
        pd.show();
        final FirebaseAuth Auth= FirebaseAuth.getInstance();

        Auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(user_login.this, "user connected", Toast.LENGTH_LONG).show();// שומר בשרד פרפרנס את הנתונים של המשתמש ומעביר את הבוליאן לטרו כדי שהוא לא ימחק אותם
                    pd.dismiss();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Email", email.getText().toString());
                    editor.putString("Pass", pass.getText().toString());
                    if(rem.isChecked())
                    {
                        editor.putBoolean("isChecked", true);
                    }
                    editor.commit();

                    Intent in=new Intent(user_login.this, MainActivity.class);
                    in.putExtra("from user_login", true);
                    startActivity(in);
                }
            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(user_login.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        password = pass.getText().toString();
        mail = email.getText().toString();
        Name = name.getText().toString();
        logIn();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.guest_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);
        int id=item.getItemId();

        if(id==R.id.logIn)
        {
            Intent i=new Intent(this, user_login.class);
            startActivity(i);
        }
        else if(id==R.id.signup)
        {
            Intent i=new Intent(this, UserRegister.class);
            startActivity(i);
        }
        else if(id==R.id.logout)
        {
            Intent i=new Intent(this, UserRegister.class);
            startActivity(i);
        }
        return true;
    }
    }

