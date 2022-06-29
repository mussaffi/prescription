package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class doc_login extends AppCompatActivity implements View.OnClickListener {
    EditText name, email, pass;
    Button b3;
    String docPassword;
    String docMail;
    String docName;
    SharedPreferences sp;
    CheckBox rem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_login);
        name = findViewById(R.id.loginName);
        email = findViewById(R.id.editTextTextEmailAddress);
        pass = findViewById(R.id.editTextTextPassword);
        b3 = findViewById(R.id.button3);
        b3.setOnClickListener(this);
        sp = getSharedPreferences("Doctors", MODE_PRIVATE);
        rem = findViewById(R.id.RememberMe);
    }

    public void logIn(){
        final ProgressDialog pd=ProgressDialog.show(this, "connecting", "please wait", true);
        pd.show();
        final FirebaseAuth Auth= FirebaseAuth.getInstance();

        Auth.signInWithEmailAndPassword(docMail, docPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(doc_login.this, "Doctor connected", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Email", email.getText().toString());
                    editor.putString("Pass", pass.getText().toString());
                    editor.commit();
                    if(rem.isChecked())
                    {
                        sp.edit();
                        editor.putString("Email", email.getText().toString());
                        editor.putString("Pass", pass.getText().toString());
                        editor.putBoolean("isChecked", true);
                        editor.commit();
                    }

                    Intent in=new Intent(doc_login.this, MainActivity.class);
                    in.putExtra("from doc_login", true);
                    startActivity(in);
                }
            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(doc_login.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        docPassword = pass.getText().toString();
        docMail = email.getText().toString();
        docName = name.getText().toString();
        logIn();
    }
}