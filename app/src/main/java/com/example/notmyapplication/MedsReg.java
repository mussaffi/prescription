package com.example.notmyapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MedsReg extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddMed";
    EditText name, price;
    Button b3;
    ImageView pic;
    String picName;
    Uri uri;

    StorageReference mStorageRef;


    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ProgressDialog p;

    DatabaseReference myref = firebaseDatabase.getReference("Meds");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meds_reg);
        name = findViewById(R.id.loginName);

        price = findViewById(R.id.editTextTextPrice);
        b3 = findViewById(R.id.button3);
        b3.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        pic = findViewById(R.id.UPic);
        pic.setOnClickListener(this);
    }

    public void CreateMed() {
        p = new ProgressDialog(this);
        p.setMessage("entering Data...");
        p.show();
        double price_per_med = Double.parseDouble(price.getText().toString());
       // יצירת אובייקט מסוג med
      Meds m = new Meds(name.getText().toString(),
              Double.parseDouble(price.getText().toString()),picName);
      handlePermission();


       myref.child(name.getText().toString()).setValue(m);




        p.dismiss();
        Toast.makeText(MedsReg.this,
                "ההכנסה הצליחה", Toast.LENGTH_LONG).show();
        Intent i= new Intent(MedsReg.this, MainActivity.class);
        startActivity(i);
    }

    private void handlePermission()
    {
            Toast.makeText(MedsReg.this, "הרשאות",
                    Toast.LENGTH_LONG).show();
            mStorageRef = FirebaseStorage.getInstance()
                    .getReference("Image/meds/" + name.getText().toString());
            mStorageRef = mStorageRef.child(picName);
            mStorageRef.putFile(uri).addOnSuccessListener
                    (new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Toast.makeText(MedsReg.this, "תמונה הועלתה",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(MedsReg.this,
                            "" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }


    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        // There are no request codes
                        Intent data = result.getData();
                        uri = data.getData();
                        if (uri != null)
                        {
                            pic.setImageURI(uri);

                            //מתן שם לתמונה
                            picName = System.currentTimeMillis() + "." + getFileExtension(uri);
                        }
                    }
                }
            });
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 100 && data != null && data.getData() != null)
            {
                uri = data.getData();
                if (uri != null)
                {
                    pic.setImageURI(uri);

                    //מתן שם לתמונה
                    picName = System.currentTimeMillis() + "." + getFileExtension(uri);
                    //שמירת התמונה תתבצע תחת תיקיית
                    // Image/VegiHelper/vegi name
                    // התמונה נשמרת האימייל של המשתמש
                    mStorageRef = FirebaseStorage.getInstance()
                            .getReference("Image/meds/" + name.getText().toString());
                    mStorageRef = mStorageRef.child(picName);
                    mStorageRef.putFile(uri).addOnSuccessListener
                            (new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    Toast.makeText(MedsReg.this, "תמונה הועלתה",
                                            Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(MedsReg.this,
                                    "" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view == b3)
        {
            CreateMed();
        }

        if (view == pic)
        {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(Intent.createChooser(i, "select picture"), 100);
            someActivityResultLauncher.launch(i);
        }


    }


}