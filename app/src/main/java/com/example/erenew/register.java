package com.example.erenew;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "register";
    ///Views
    private EditText plate,ic,fname,phone;
    private TextView expired ;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Spinner option,option2,option3;
    private Button register,plan1,plan2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        Toolbar toolbar = findViewById(R.id.toolbar);
        plate = (EditText)findViewById(R.id.platenum);
        ic = (EditText)findViewById(R.id.ic);
        fname = (EditText)findViewById(R.id.custname);
        phone = (EditText)findViewById(R.id.phonenum);
        plan1 = (Button)findViewById(R.id.axa);
        plan2 = (Button)findViewById(R.id.etiqa);

        plan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this, plan.class);
                startActivity(i);
            }
        });

        plan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this, plan2.class);
                startActivity(i);
            }
        });

        expired = (TextView)findViewById(R.id.date);
        expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        register.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,mDateSetListener,year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: "+dayOfMonth+month+year);
                String date = dayOfMonth + "/" + month + "/" +year;
                expired.setText(date);
            }
        };
        //select option company
        option = (Spinner)findViewById(R.id.company);
        final ArrayList <String> arrayList = new ArrayList<>();
        arrayList.add("Sila pilih syarikat insurance");
        arrayList.add("Etiqa Takaful");
        arrayList.add("Axa Affin");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        option.setAdapter(arrayAdapter);
        option.setOnItemSelectedListener(this);

        //select option duration
        option2 = (Spinner)findViewById(R.id.type);
        final ArrayList <String> durationlist = new ArrayList<>();
        durationlist.add("Sila pilih tempoh untuk memperbaharui cukai jalan");
        durationlist.add("6 Bulan");
       durationlist.add("12 Bulan");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, durationlist);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        option2.setAdapter(arrayAdapter1);
        option2.setOnItemSelectedListener(this);

        //select option insurance type
        option3 = (Spinner)findViewById(R.id.insurancetype);
        final ArrayList <String> typeList = new ArrayList<>();
        typeList.add("Sila pilih jenis insurance anda");
        typeList.add("Comprehensive");
        typeList.add("Third-party");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        option3.setAdapter(typeAdapter);
        option3.setOnItemSelectedListener(this);

        register = (Button)findViewById(R.id.register);

        //click button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                String vehicle = plate.getText().toString().trim();
                String nric = ic.getText().toString().trim();
                String name = fname.getText().toString().trim();
                String ph = phone.getText().toString().trim();
                String roadtax = expired.getText().toString().trim();
                String choice = option.getSelectedItem().toString().trim();
                String duration  = option2.getSelectedItem().toString().trim();
                String type = option3.getSelectedItem().toString().trim();

                if(vehicle.isEmpty())
                {
                    Toast.makeText(register.this, "Sila masukkan nombor plate anda ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (nric.isEmpty() && nric.length() < 3)
                {
                    Toast.makeText(register.this, "Sila masukkan nombor IC anda ", Toast.LENGTH_SHORT).show();
                    ic.setError("Nombor Ic mestilah melebihi 3 digit");
                    return;
                }
                else if(name.isEmpty())
                {
                    Toast.makeText(register.this, "Sila masukkan nama anda ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if( ph.length() < 9 || ph.length() > 11) {
                    phone.setError("Nombor anda mestilah mengikut 0123456789 dan melebihi 10 atau 11 digit");
                    return;
                }
                else if(ph.isEmpty())
                {
                    Toast.makeText(register.this, "Sila masukkan nombor telefon anda", Toast.LENGTH_SHORT).show();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                builder.setTitle("Anda pasti hendak mendaftar?");
                builder.setMessage("Sekiranya anda pasti, tekan ya untuk meneruskan");
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        register.setVisibility(View.VISIBLE);
                    }
                });
                builder.show();


                uploadData(vehicle,nric,name,ph,roadtax,choice,email,duration,type);



            }
        });

        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void uploadData(final String vehicle, String nric, String name, String ph, String roadtax,String choice,String email, String duration, String type) {
        final Map<String, Object> vehicleMap = new HashMap<>();
        vehicleMap.put("PlateNo", vehicle);
        vehicleMap.put("IC", nric);
        vehicleMap.put("Name",name);
        vehicleMap.put("Phone", ph);
        vehicleMap.put("ExpDate", roadtax);
        vehicleMap.put("Insurance", choice);
        vehicleMap.put("Duration", duration);
        vehicleMap.put("Email", email);
        vehicleMap.put("type", type);
        vehicleMap.put("Status", "");

        db.collection("Vehicle").document(vehicle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    plate.setError("Nombor plate telah wujud");
                    plate.requestFocus();
                }
                else
                {
                    db.collection("Vehicle").document(vehicle).set(vehicleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(register.this, "Pendaftaran Berjaya", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(register.this, Home.class);
                            startActivity(i);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(register.this, "Error: " +error, Toast.LENGTH_SHORT ).show();
                            Intent i = new Intent(register.this, register.class);
                            startActivity(i);
                        }
                    });
                }
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
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(register.this, Home.class);
            startActivity(i);
        }  else if (id == R.id.nav_register) {
            Intent in = new Intent(register.this, register.class);
            startActivity(in);

        } else if (id == R.id.nav_status) {
            Intent intent = new Intent(register.this, checkStat.class);
            startActivity(intent);
        }else if (id == R.id.nav_saman) {
            Intent intent = new Intent(register.this,checkSummon.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            Intent intent = new Intent(register.this, profile.class);
            startActivity(intent);
        }
        else if(id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(register.this, MainActivity.class);
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
        String choice = parent.getItemAtPosition(position).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
