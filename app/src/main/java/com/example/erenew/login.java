package com.example.erenew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class login extends AppCompatActivity {

    private EditText email, pass;
    private Button log;
    private TextView t,lupa;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        t = (TextView) findViewById(R.id.log);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.editPass);
        log = (Button) findViewById(R.id.login);
        lupa = (TextView)findViewById(R.id.reset);

        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, resetPass.class);
                startActivity(i);
            }
        });

        pd = new ProgressDialog(this);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                {
                    if (user != null) {
                        Toast.makeText(login.this, "Anda sedang log masuk", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(login.this, Home.class);
                        startActivity(i);
                        pd.dismiss();
                    }
                }

            }
        };

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = email.getText().toString();
                String pwd = pass.getText().toString();
                if (mail.isEmpty()) {
                    email.setError("Sila isikan email id anda");
                    email.requestFocus();
                } else if (pwd.isEmpty()) {
                    pass.setError("Sila isikan kata laluan anda");
                    pass.requestFocus();
                } else if (mail.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(login.this, "Medan Kosong!", Toast.LENGTH_SHORT).show();
                } else if (!(mail.isEmpty() && pwd.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(login.this, "Log Masuk Ralat, Sila Log Masuk Semula", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intToHome = new Intent(login.this, Home.class);
                                startActivity(intToHome);
                                //set tittle of progress dialog
                                pd.setTitle("Log Masuk...");
                                pd.setMessage("Sila Tunggu...");
                                pd.setProgressStyle(pd.STYLE_HORIZONTAL);
                                pd.setIndeterminate(true);
                                pd.setMax(100);
                                //show progress dialog
                                pd.show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(login.this, "Masalah Berlaku!", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }


    @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthStateListener);
        }

}

