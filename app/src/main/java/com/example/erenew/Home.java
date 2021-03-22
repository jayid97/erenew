package com.example.erenew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;


import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Home";

    RecyclerView mrecyclerView;
    FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private VehicleListAdapter adapter;
    ProgressDialog pd;
     private List<Vehicle> vehicles;
     private Button add, checkstat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        Log.d(TAG, "onCreate: Firebase User: "+email);
        vehicles = new ArrayList<>();
        adapter = new VehicleListAdapter(vehicles,this);
        mrecyclerView = (RecyclerView) findViewById(R.id.plate_list);
        add = (Button)findViewById(R.id.addBtn);
        checkstat = (Button)findViewById(R.id.check);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setAdapter(adapter);

        pd = new ProgressDialog(this);

        if (user != null) {

        } else {
            Toast.makeText(Home.this, "Ralat! Sila log masuk semula", Toast.LENGTH_SHORT).show();
            Intent intToHome = new Intent(Home.this, login.class);
            startActivity(intToHome);
        }
        //show data in recyclerview
        showData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, register.class);
                startActivity(intent);
            }
        });

        checkstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, checkStat.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void showData() {
        //set tittle of progress dialog
        pd.setTitle("Memuatkan data...");
        pd.setProgressStyle(pd.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setMax(100);
        //show progress dialog
        pd.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                pd.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        mFireStore.collection("Vehicle").whereEqualTo("Email", email).whereEqualTo("Status", "")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
            if(e!=null)
            {
                Log.d(TAG, "Error:"+ e.getMessage());
            }
            for (DocumentChange doc: documentSnapshots.getDocumentChanges())
            {
                if(doc.getType() == DocumentChange.Type.ADDED)
                {
                    pd.dismiss();
                    Vehicle vehicle = doc.getDocument().toObject(Vehicle.class);
                    vehicles.add(vehicle);
                    adapter.notifyDataSetChanged();
                }
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_home) {
            intent = new Intent(Home.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            intent = new Intent(Home.this, register.class);
            startActivity(intent);

        } else if (id == R.id.nav_saman) {
            intent = new Intent(Home.this, checkSummon.class);
            startActivity(intent);
        }        else if (id == R.id.nav_status) {
            intent = new Intent(Home.this, checkStat.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            intent = new Intent(Home.this, profile.class);
            startActivity(intent);
        }
        else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
             intent = new Intent(Home.this, MainActivity.class);
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


}
