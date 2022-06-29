package com.example.notmyapplication;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {
    public static final int PICK_FROM_GALLERY = 1;

    EditText name;
    EditText pass;
    EditText email;
    EditText phone;
    String num="  ";
    ImageView pic;
    String picName;
    Uri uri;
    final int FROM_GALLERY =1;
    final int FROM_CAMERA =2;
    int flag=0;
    byte[] bytes;
    boolean f1=false;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    AlertDialog.Builder adb;
    AlertDialog ad;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    StorageReference mStorageRef;
    Button b;
    ProgressDialog p;
    // ImageView pic;

    DatabaseReference myref = firebaseDatabase.getReference("users").push();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma3_reg);

        name = findViewById(R.id.SP_name);
        pass = findViewById(R.id.SP_password);
        email = findViewById(R.id.SP_email);
        phone = findViewById(R.id.editTextPhone);

        pic = findViewById(R.id.user_PIC);
        pic.setOnClickListener(this);

        b = findViewById(R.id.SignApp);
        b.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void CreateUser() {
        p = new ProgressDialog(this);
        p.setMessage("Registration...");
        if (isValidate())
            p.show();

        if (isValidate())
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString
                    (), pass.getText().toString()).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //יצירת אובייקט מסוג User
                                user u = new user(name.getText().toString(),
                                        pass.getText().toString(),
                                        email.getText().toString(),
                                        phone.getText().toString(),
                                        picName,
                                        myref.getKey());
                                handlePermission();



                                myref.setValue(u);


                                p.dismiss();
                                Toast.makeText(UserRegister.this,
                                        "ההרשמה הצליחה", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(UserRegister.this, user_login.class);
                                i.putExtra("from signUp", true);
                                startActivity(i);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserRegister.this, " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    p.dismiss();
                }
            });
    }

    public boolean isValidate() {
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid email");
            email.setFocusable(true);
            return false;
        } else if (pass.getText().toString().length() < 6) {
            pass.setError("password length at least 6 characters");
            pass.setFocusable(true);
            return false;
        } else if (email.getText().toString() == null) {
            email.setError("pls enter email");
            email.setFocusable(true);
            return false;
        } else if (pass.getText().toString() == null) {
            pass.setError("pls enter password");
            pass.setFocusable(true);
            return false;
        }
        else if (phone.getText().toString().equals(null)) {
            phone.setError("please enter phone numebr");
            phone.setFocusable(true);
            return false;
        }
        else if (!phone.getText().toString().equals(null)&&(!num.equals("05")||phone.getText().toString().length()!=10)) {
            phone.setError("phone number is not valid");
            phone.setFocusable(true);
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        if (v == b) {
            if(!phone.getText().toString().equals(""))
                num=phone.getText().toString().substring(0,2);
            CreateUser();
        }
        if (v == pic) {

                adb = new AlertDialog.Builder(UserRegister.this);
                adb.setTitle("בחירת תמונה");
                adb.setMessage("אתה הולך לבחור תמונה מהגלריה או מהמצלמה");
                adb.setCancelable(true);
                adb.setPositiveButton("גלריה", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface d, int i) {
                        Intent intent = new Intent();
                        intent.setType("image/*");

                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        activityResultLauncher.launch(intent);
                        //  startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);

                    }
                });
                adb.setNeutralButton("מצלמה", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface d, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                            if (checkSelfPermission(Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                            }
                        } else {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            activityResultLauncher.launch(intent);
                        }
                    }
                });
                ad = adb.create();
                ad.show();
            }
        }


    ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getData()!=null && result.getData().getAction()==null &&result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uri = data.getData();
                        if(uri != null) {
                            pic.setImageURI(uri);
                            picName = System.currentTimeMillis() + "." + getFileExtension(uri);
                        }
                        flag=FROM_GALLERY;
                        f1= true;
                    }

                    else if (result.getData()!=null   && result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(UserRegister.this,result.getData().getData()+" ,",Toast.LENGTH_LONG).show();
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");

                        picName=System.currentTimeMillis() + "."+ "jpg";
                        pic.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        bytes = baos.toByteArray();
                        flag=FROM_CAMERA;
                        f1= true;
                    }
                }
            });


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
            Intent i = new Intent(this, UserRegister.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            Intent i = new Intent(this, UserRegister.class);
            startActivity(i);
        }
        return true;
    }

    private void handlePermission() {

        Toast.makeText(UserRegister.this, "הרשאות",
                Toast.LENGTH_LONG).show();
        mStorageRef = FirebaseStorage.getInstance()
                .getReference("Image/Users/" + email.getText().toString());
        mStorageRef = mStorageRef.child(picName);

        if (flag == FROM_GALLERY) {
            mStorageRef.putFile(uri).addOnSuccessListener
                    (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UserRegister.this, "תמונה הועלתה מהגלריה",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserRegister.this,
                            "" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            StorageTask<UploadTask.TaskSnapshot> uploadTask = mStorageRef.putBytes(bytes).addOnSuccessListener
                    (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UserRegister.this, "תמונה הועלתה מהמצלמה",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserRegister.this,
                            "" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }
        }


    private String getFileExtension(Uri uri)//תמצא את הקישור של התמונה(uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100 && data != null && data.getData() != null) {
                uri = data.getData();
                if (uri != null) {
                    pic.setImageURI(uri);

                    //מתן שם לתמונה
                    picName = System.currentTimeMillis() + "." + getFileExtension(uri);
                    //שמירת התמונה תתבצע תחת תיקיית
                    // Image/VegiHelper/vegi name
                    // התמונה נשמרת האימייל של המשתמש
                    mStorageRef = FirebaseStorage.getInstance()
                            .getReference("Image/Users/" + email.getText().toString());
                    mStorageRef = mStorageRef.child(picName);
                    mStorageRef.putFile(uri).addOnSuccessListener
                            (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(UserRegister.this, "תמונה הועלתה",
                                            Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserRegister.this,
                                    "" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }
}
