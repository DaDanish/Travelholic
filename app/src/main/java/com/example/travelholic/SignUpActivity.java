package com.example.travelholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelholic.Modal.Users;
import com.example.travelholic.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding activitySignUpBinding;

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("Please wait we are creating your account");

        activitySignUpBinding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(activitySignUpBinding.etSignUpEmail.getText().toString(),
                        activitySignUpBinding.etSignUpPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            String fName = activitySignUpBinding.etSignUpFirstName.getText().toString().substring(0,1).toUpperCase() + activitySignUpBinding.etSignUpFirstName.getText().toString().substring(1).toLowerCase();
                            String lName = activitySignUpBinding.etSignUpLastName.getText().toString().substring(0,1).toUpperCase() + activitySignUpBinding.etSignUpLastName.getText().toString().substring(1).toLowerCase();

                            Users user = new Users(fName,lName, activitySignUpBinding.etSignUpEmail.getText().toString().trim(),activitySignUpBinding.etSignUpPassword.getText().toString().trim());

                            firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                }
                            });
                            Toast.makeText(SignUpActivity.this, "User created successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });




    }
}