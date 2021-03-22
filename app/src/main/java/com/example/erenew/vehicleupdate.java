package com.example.erenew;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class vehicleupdate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "vehicleupdate";
    private EditText ic,fname,phone;
    private TextView plate;
    private Button update;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicleupdate2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        vehicle = (Vehicle)getIntent().getSerializableExtra("Vehicle");
        final String Name = getIntent().getStringExtra("Name");
        final String nric = getIntent().getStringExtra("IC");
        final String ph = getIntent().getStringExtra("Phone");
        final String plat = getIntent().getStringExtra("PlateNo");

        plate = (TextView)findViewById(R.id.editPlate);
        ic = (EditText)findViewById(R.id.editIc);
        fname = (EditText)findViewById(R.id.editname);
        phone = (EditText)findViewById(R.id.editPhone);

        fname.setText(Name);
        ic.setText(nric);
        plate.setText(plat);
        phone.setText(ph);





        update = (Button)findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(vehicleupdate.this);
                builder.setTitle("Anda pasti untuk kemas kini ?");
                builder.setMessage("Sekiranya anda pasti, sila tekan ya untuk meneruskan");
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update.setVisibility(View.VISIBLE);
                    }
                });
                builder.show();
                switch (v.getId()) {
                    case R.id.update:
                        updateProduct();
                        break;
                }
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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

    private boolean hasValidationErrors(String Name, String Ic, String Phone , String PlateNo) {
        if (Name.isEmpty()) {
            fname.setError("Nama diperlukan");
            fname.requestFocus();
            return true;
        }

        if (Ic.isEmpty()) {
            ic.setError("Ic diperlukan");
            ic.requestFocus();
            return true;
        }

        if (Phone.isEmpty()) {
           phone.setError("No Telefon diperlukan");
           phone.requestFocus();
            return true;
    }

        if (PlateNo.isEmpty()) {
            plate.setError("No Plat diperlukan");
            plate.requestFocus();
            return true;
        }

        return false;
    }

    private void updateProduct() {
        String nama = fname.getText().toString();
        String icnum = ic.getText().toString();
        String pl = plate.getText().toString();
        String phonenum = phone.getText().toString();
        DocumentReference vehicleRef = db.collection("Vehicle").document(pl);
          vehicleRef.update(
                     "IC",icnum,
                        "Name",nama,
                        "Phone",phonenum,
                        "PlateNo",pl
                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(vehicleupdate.this,"Kemas kini berjaya",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(vehicleupdate.this, Home.class);
                startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(vehicleupdate.this, "Kemas kini gagal",Toast.LENGTH_SHORT).show();
                }
            });

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vehicleupdate, menu);
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
            Intent i = new Intent(vehicleupdate.this, Home.class);
            startActivity(i);
        }  else if (id == R.id.nav_register) {
            Intent in = new Intent(vehicleupdate.this, register.class);
            startActivity(in);

        }
        else if (id == R.id.nav_status) {
            Intent intent = new Intent(vehicleupdate.this, checkStat.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_saman) {
            Intent intent = new Intent(vehicleupdate.this,checkSummon.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            Intent intent = new Intent(vehicleupdate.this, profile.class);
            startActivity(intent);
        }
        else if(id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(vehicleupdate.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


