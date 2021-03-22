package com.example.erenew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPass extends AppCompatActivity {

    private static final String TAG = "reset";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText mailReset;
    private Button hantar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);


        mailReset = (EditText)findViewById(R.id.emailR);
       hantar = (Button)findViewById(R.id.send);


        hantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail= mailReset.getText().toString().trim();
                Log.d(TAG, "email: " +mail);
                auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(resetPass.this, "Permohonan berjaya ! Sila semak email anda untuk menukarkan kata laluan", Toast.LENGTH_SHORT).show();
                            Intent intToHome = new Intent(resetPass.this, login.class);
                            startActivity(intToHome);
                        }
                    }
                });
            }
        });


    }
}
