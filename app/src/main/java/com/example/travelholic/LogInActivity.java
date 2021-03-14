package com.example.travelholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelholic.Modal.Users;
import com.example.travelholic.databinding.ActivityLogInBinding;
import com.example.travelholic.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding activityLogInBinding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInClient mGoogleSignInClient;

    ImageView ivLoginGoogle;

    final int RC_SIGN_IN = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogInBinding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(activityLogInBinding.getRoot());

        ivLoginGoogle = findViewById(R.id.ivLoginGoogle);

        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Logging into your account");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }






        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);





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





        ivLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });






    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }





    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            Users newUser = new Users();
                            String fullName= user.getDisplayName();
                            String lastName = fullName.split(" ")[fullName.split(" ").length-1];
                            String firstName = fullName.substring(0, fullName.length() - lastName.length());

                            newUser.setUserID(user.getUid());
                            newUser.setFirstName(firstName.trim());
                            newUser.setLastName(lastName.trim());
                            newUser.setProfileImage(user.getPhotoUrl().toString());

                            firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(newUser);


                            startActivity(new Intent(LogInActivity.this,MainActivity.class));
                            Toast.makeText(LogInActivity.this, "Login with Gmail", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                        }
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


















