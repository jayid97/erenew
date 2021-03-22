package com.example.erenew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity implements View.OnClickListener {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference("profile/");
    private ImageView imgPreview;
    private Uri imgUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = "signup";
    private EditText editEmail, editPassword, fname, phone;
    private Button buttonRegister, upload;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.editPassword);
        fname = (EditText) findViewById(R.id.fname);
        phone = (EditText) findViewById(R.id.phone);

        imgPreview = (ImageView) findViewById(R.id.imageView);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
        upload = (Button)findViewById(R.id.upload_pic);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

     public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Memilih gambar"),PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgUri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            imgPreview.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {

    }
        else {

    }
}

    @Override
    public void onClick(View v) {
        final String name = fname.getText().toString().trim();
        final String phoneNum = phone.getText().toString().trim();
        final String email = editEmail.getText().toString();
        final String pass = editPassword.getText().toString();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(signup.this, "Sila Masukkan Email Anda ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass) || pass.length() > 6) {
            Toast.makeText(signup.this, "Sila Masukkan Kata Laluan  Anda", Toast.LENGTH_SHORT).show();
            editPassword.setError("Kata Laluan Anda Tidak Melebihi 6 aksara");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(signup.this, "Sila Masukkan Nama Anda  ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNum)|| phoneNum.length() < 10) {
            Toast.makeText(signup.this, "Sila Masukkan Nombor Telefon Anda  ", Toast.LENGTH_SHORT).show();
            phone.setError("Nombor Telefon Anda Mestilah 10 digit dan keatas");
            return;
        }

        if(imgUri != null){
            Log.d(TAG, "onClick: image");

                final ProgressDialog progressDialog = new ProgressDialog(signup.this);
            final ProgressDialog progressDialog1 = new ProgressDialog(signup.this);
            progressDialog1.setTitle("Sedang disemak....");
            progressDialog1.show();
            final StorageReference ref = storageReference.child(System.currentTimeMillis()+ "."+getFileExtension(imgUri));
            uploadTask = ref.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    Log.d(TAG, "onSuccess: uri= "+ uri.toString());

                                    final Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("Name", name);
                                    userMap.put("Email", email);
                                    userMap.put("imgUri",uri.toString());
                                    userMap.put("UserLevel", "Customer");
                                    userMap.put("Phone", phoneNum);
                                    db.collection("User").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if(task.getResult().isEmpty()){
                                                    db.collection("User").document(email).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    progressDialog1.dismiss();

                                                                    if(task.isSuccessful())
                                                                    {
                                                                        progressDialog.setTitle("Memuat Naik....");
                                                                        progressDialog.show();
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(signup.this, "Pendaftaran Berjaya", Toast.LENGTH_SHORT).show();
                                                                        Intent i = new Intent(signup.this, login.class);
                                                                        startActivity(i);
                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(signup.this,"Email telah wujud",Toast.LENGTH_SHORT).show();
                                                                    }


                                                                }
                                                            });
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            String error = e.getMessage();
                                                            Toast.makeText(signup.this, "Ralat: " +error, Toast.LENGTH_SHORT ).show();

                                                        }
                                                    });
                                                }
                                                else {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if (document.exists()) {
                                                            Toast.makeText(signup.this, "Email telah wujud", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(signup.this,"Gagal "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Memuat Naik "+(int)progress+"%");
                            progressDialog1.setMessage("Data sedang disemak "+(int)progress+"%");
                        }
                    });

            Log.d(TAG, "onClick: upload task "+uploadTask);
        } else {
            Toast.makeText(signup.this, "Tiada gambar yang dipilih", Toast.LENGTH_SHORT).show();
        }





    }

    }







