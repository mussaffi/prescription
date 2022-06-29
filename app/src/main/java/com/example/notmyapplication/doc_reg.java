package com.example.notmyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class doc_reg extends AppCompatActivity implements View.OnClickListener {
    EditText docName;
    EditText docPass;
    EditText docEmail;
    EditText docPhone;
String num="  ";
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Button b;
    ProgressDialog p;

    DatabaseReference myref = firebaseDatabase.getReference("Doctors").push();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma3_reg_doc);
        b = findViewById(R.id.SignApp);
        b.setOnClickListener(this);
        docName = findViewById(R.id.SP_name);
        docPass = findViewById(R.id.SP_password);
        docEmail = findViewById(R.id.SP_email);
        docPhone = findViewById(R.id.editTextPhone2);
        firebaseAuth = FirebaseAuth.getInstance();

    }
    public void createDoctor(){


        p = new ProgressDialog(this);
        p.setMessage("Registration...");

        if (isValidate())
        p.show();
        if (isValidate())
        firebaseAuth.createUserWithEmailAndPassword(docEmail.getText().toString
                    (), docPass.getText().toString()).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //יצירת אובייקט מסוג doc
                                Doc u = new Doc(docName.getText().toString(),
                                        docPass.getText().toString(),
                                        docEmail.getText().toString(),
                                        docPhone.getText().toString(),
                                        myref.getKey());

                                myref.setValue(u);
                                p.dismiss();
                                Toast.makeText(doc_reg.this,
                                        "reg succesful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(doc_reg.this, doc_login.class);
                                i.putExtra("from signUp", true);
                                startActivity(i);
                                finish();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(doc_reg.this, " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    p.dismiss();
                }
            });
    }
    public boolean isValidate() { // דיקה של האם הנתונים שהוכנסו של השדות חוקיים
        if (!Patterns.EMAIL_ADDRESS.matcher(docEmail.getText().toString()).matches()) {
            docEmail.setError("Invalid email");
            docEmail.setFocusable(true);
            return false;
        } else if (docPass.getText().toString().length() < 6) {
            docPass.setError("password length at least 6 characters");
            docPass.setFocusable(true);
            return false;
        } else if (docEmail.getText().toString() == null) {
            docEmail.setError("pls enter email");
            docEmail.setFocusable(true);
            return false;
        } else if (docPass.getText().toString() == null) {
            docPass.setError("pls enter password");
            docPass.setFocusable(true);
            return false;
        }
        else if (docEmail.getText().toString() == null) {
            docEmail.setError("please enter email");
            docEmail.setFocusable(true);
            return false;
        } else if (docPass.getText().toString() == null) {
            docPass.setError("please enter password");
            docPass.setFocusable(true);
            return false;
        }
        else if (docPhone.getText().toString().equals(null)) {
            docPhone.setError("please enter phone numebr");
            docPhone.setFocusable(true);
            return false;
        }
        else if (!docPhone.getText().toString().equals(null)&&(!num.equals("05")||docPhone.getText().toString().length()!=10)) {
            docPhone.setError("phone number is not valid");
            docPhone.setFocusable(true);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == b) {
            if(!docPhone.getText().toString().equals(""))
                num=docPhone.getText().toString().substring(0,2);
            createDoctor();



        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guest_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
}
