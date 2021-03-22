package com.example.erenew;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class profileUpdate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference("profil/");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText phone,fname;
    private TextView email;
    private ImageView pic;
    private Button update,upload;
    private Uri imgUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageTask uploadTask;
    private static final String TAG = "profileUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        final String Name = getIntent().getStringExtra("name");
        final String ph = getIntent().getStringExtra("phone");
        final String mail = getIntent().getStringExtra("email");
        final String dp = getIntent().getStringExtra("imgUri");

        fname = (EditText)findViewById(R.id.editname);
        phone = (EditText)findViewById(R.id.editPhone);
        email = (TextView)findViewById(R.id.textEmail);
        pic = (ImageView)findViewById(R.id.imageView);

        fname.setText(Name);
        phone.setText(ph);
        email.setText(mail);
        Picasso.get().load(dp).into(pic);

        update = (Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }


        });
    }

    private void updateProfile() {
        final String name = fname.getText().toString().trim();
        final String phoneNum = phone.getText().toString().trim();
        String mail = email.getText().toString().trim();
        DocumentReference vehicleRef = db.collection("User").document(mail);
        vehicleRef.update(
                "Name",name,
                "Phone",phoneNum
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(profileUpdate.this,"Kemas kini berjaya",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(profileUpdate.this, profile.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profileUpdate.this, "Kemas kini gagal",Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(profileUpdate.this, Home.class);
            startActivity(i);
        }  else if (id == R.id.nav_register) {
            Intent in = new Intent(profileUpdate.this, register.class);
            startActivity(in);

        } else if (id == R.id.nav_status) {
            Intent intent = new Intent(profileUpdate.this,checkStat.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_saman) {
            Intent intent = new Intent(profileUpdate.this,checkSummon.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            Intent intent = new Intent(profileUpdate.this, profile.class);
            startActivity(intent);
        }

        else if(id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent I = new Intent(profileUpdate.this, MainActivity.class);
            startActivity(I);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
