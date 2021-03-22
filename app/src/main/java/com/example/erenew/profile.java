package com.example.erenew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "profile";
    RecyclerView mrecyclerView;
    ProgressDialog pd;
    FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private profileListAdapter adapter;
    private List<User> pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        Log.d(TAG, "onCreate: Firebase User: "+email);
        pay = new ArrayList<>();
        adapter = new profileListAdapter(pay,this);
        mrecyclerView = (RecyclerView) findViewById(R.id.profile_list);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setAdapter(adapter);
        pd = new ProgressDialog(this);
        showData();
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
        pd.setTitle("Loading Data...");
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
        mFireStore.collection("User").whereEqualTo("Email", email)
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
                                User user = doc.getDocument().toObject(User.class);
                                pay.add(user);
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
        getMenuInflater().inflate(R.menu.profile, menu);
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
            Intent i = new Intent(profile.this, Home.class);
            startActivity(i);
        }  else if (id == R.id.nav_register) {
            Intent in = new Intent(profile.this, register.class);
            startActivity(in);

        } else if (id == R.id.nav_status) {
            Intent intent = new Intent(profile.this,checkStat.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_saman) {
            Intent intent = new Intent(profile.this,checkSummon.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            Intent intent = new Intent(profile.this, profile.class);
            startActivity(intent);
        }

        else if(id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent I = new Intent(profile.this, MainActivity.class);
            startActivity(I);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
