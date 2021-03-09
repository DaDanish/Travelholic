package com.example.travelholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelholic.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding activityLogInBinding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogInBinding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(activityLogInBinding.getRoot());

        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Logging into your account");

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }


        activityLogInBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateLoginEmail() | !validateLoginPassword())
                {
                    Toast.makeText(LogInActivity.this, "Please clear errors", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(activityLogInBinding.etLoginEmail.getText().toString().trim(),activityLogInBinding.etLoginPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.show();
                                        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        progressDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



            }
        });




        activityLogInBinding.tvLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });



    }




    private boolean validateLoginEmail() {
        String usernameInput = activityLogInBinding.etLoginEmail.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            activityLogInBinding.etLoginEmail.setError("Field can't be empty");
            return false;
        } else {
            activityLogInBinding.etLoginEmail.setError(null);
            return true;
        }
    }





    private boolean validateLoginPassword() {
        String emailInput = activityLogInBinding.etLoginPassword.getText().toString().trim();
        if (emailInput.isEmpty()) {
            activityLogInBinding.textInputLoginPassword.setError("Field can't be empty");
            return false;
        } else {
            activityLogInBinding.textInputLoginPassword.setError(null);
            return true;
        }
    }



}


















